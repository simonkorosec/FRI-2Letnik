import json
import sys
import requests
import dateutil.parser
from datetime import datetime, timezone


# Id postaje
def get_station_id(station_name):
    r = requests.get("http://data.lpp.si/stations/getAllStations")
    int_id = []
    station_name = station_name.lower().strip()
    if not r.json()["success"]:
        print("Unknown station name " + station_name)
        exit(1)
    for s in r.json()["data"]:
        ime = str(s["name"]).lower()
        if ime == station_name:
            int_id.append(s["int_id"])
    return int_id


# Poti k grejo čez postajo
def get_route_on_station(station_id):
    par = {"station_int_id": station_id}
    r = requests.get("http://data.lpp.si/stations/getRoutesOnStation", params=par)
    return r.json()["data"]


# Pomožna za spored
def _arrival_on_station(station_id, rout_id):
    par = {"station_int_id": station_id, "route_int_id": rout_id}
    r = requests.get("http://data.lpp.si/timetables/getArrivalsOnStation", params=par)
    if r.json()["success"]:
        return r.json()["data"]
    else:
        return [None]


# Spored
def get_arrival_on_station(station_name, line_num=-1, after_time=None):
    station_ids = get_station_id(station_name)
    neki = 0
    for x in station_ids:
        neki = 10
        routes = get_route_on_station(x)
        for route in routes:
            izpis = 0
            if line_num == -1 or int(route["route_group_number"]) == line_num:
                for t in _arrival_on_station(x, int(route["int_id"])):
                    if t is None:
                        izpis = -13
                        continue

                    if izpis == 0:
                        print("name: {}, number: {}".format(route["route_name"], route["route_group_number"]))
                        izpis = -13

                    date = dateutil.parser.parse(t["arrival_time"])
                    if after_time is None:
                        now = datetime.now(timezone.utc)
                    else:
                        d = datetime.strptime(after_time, '%H:%M')
                        now = datetime.now(timezone.utc).replace(hour=d.hour, minute=d.minute)

                    if date > now:
                        print(date.strftime("%H:%M"))
    if neki == 0:
        print("No stations found with name: '" + station_name.strip() + "'")
        exit(0)


# Live arrival
def get_live_arrival(station_id):
    par = {"station_int_id": station_id}
    r = requests.get("http://data.lpp.si/timetables/liveBusArrival", params=par)
    return r.json()["data"]


# Live arrival
def arrivals_to_station(station_name, line_num=-1):
    station_ids = get_station_id(station_name)
    neki = 0
    for x in station_ids:
        neki = 10
        data = get_live_arrival(x)
        for y in data:
            if line_num == -1 or int(y["route_number"]) == line_num:
                print("Trola {} smer {} eta {}".format(y["route_number"], y["route_name"], y["eta"]))

    if neki == 0:
        print("No stations found with name: '" + station_name.strip() + "'")
        exit(0)


def write_data(station_name="", line_num=-1, after_time=None):
    data = {"station_name": station_name, "line_num": line_num, "after_time": after_time}
    with open('_data.txt', 'w') as outfile:
        json.dump(data, outfile)


stat_name = ""
line = -1
time = None

if len(sys.argv) > 1:
    if str(sys.argv[1]).lower() == "arrivals":
        i = 2
        while i < len(sys.argv):
            ukaz = str(sys.argv[i]).lower()
            if ukaz == "to":
                i += 1
                while i < len(sys.argv):
                    sr = str(sys.argv[i]).lower()
                    if sr == "line" or sr == "at":
                        i -= 1
                        break
                    else:
                        stat_name += sr + " "
                        i += 1
            elif ukaz == "line":
                i += 1
                sr = str(sys.argv[i]).lower()
                if sr == "to" or sr == "at":
                    i -= 1
                    break
                else:
                    line = int(sr)
                    i += 1
            elif ukaz == "at":
                i += 1
                sr = str(sys.argv[i]).lower()
                if sr == "to" or sr == "line":
                    i -= 1
                    break
                else:
                    time = sr
                    i += 1
            else:
                i += 1

        get_arrival_on_station(station_name=stat_name, line_num=line, after_time=time)
    elif str(sys.argv[1]).lower() == "departures":
        i = 2
        while i < len(sys.argv):
            ukaz = str(sys.argv[i]).lower()
            if ukaz == "from":
                i += 1
                while i < len(sys.argv):
                    sr = str(sys.argv[i]).lower()
                    if sr == "line":
                        i -= 1
                        break
                    else:
                        stat_name += sr + " "
                        i += 1
            elif ukaz == "line":
                i += 1
                sr = str(sys.argv[i]).lower()
                if sr == "from":
                    i -= 1
                    break
                else:
                    line = int(sr)
                    i += 1
            else:
                i += 1

        arrivals_to_station(station_name=stat_name, line_num=line)

    else:
        print("Unknown arguments. Try:")
        print("arrivals to (station name) [line (line number) at (time in format H:M)]")
        print("departures from (station name) [line (line number)]")
        exit()

    write_data(stat_name, line, time)
