import sys
import requests
import json

api_key = "AIzaSyBsPC63YFK5z05Ab2krX1rM_Ry-KTaEdyc"


def write_data(station_name="", line_num=-1, after_time=None):
    data = {"station_name": station_name, "line_num": line_num, "after_time": after_time}
    with open('_data.txt', 'w', encoding='utf8') as outfile:
        json.dump(data, outfile, ensure_ascii=False)


def route_to_from(od, do):
    r = requests.get("https://maps.googleapis.com/maps/api/directions/json?origin=" + od + "&destination=" + do + "&mode=transit&alternatives=true&key=" + api_key + "")
    routes = r.json()["routes"]

    k = 0
    j = 0

    for ro in routes:
        naj = int(str(routes[k]["legs"][0]["duration"]["text"]).split(" ")[0])
        t = int(str(ro["legs"][0]["duration"]["text"]).split(" ")[0])

        if t < naj:
            k = j

        j += 1

    eta = routes[k]["legs"][0]["duration"]["text"]  # dolžina poti

    for step in routes[k]["legs"][0]["steps"]:
        if step["travel_mode"] == "TRANSIT":
            direction = step["transit_details"]["headsign"]  # smer
            izstop = step["transit_details"]["arrival_stop"]["name"]  # izstop če je bus
            line_num = step["transit_details"]["line"]["short_name"]  # št trole
            print("Take the bus line {} towards {} and get off at {}.".format(line_num, direction, izstop))
        elif step["travel_mode"] == "WALKING":
            navodila = step["html_instructions"]  # navodila
            print(navodila + ".")

    print("Estimated time of journey " + eta + ".")
    exit(0)


to = ""
from_loc = ""
if len(sys.argv) > 1:
    i = 1
    while i < len(sys.argv):
        ukaz = str(sys.argv[i]).lower()

        if ukaz == "to":
            i += 1
            while i < len(sys.argv):
                sr = str(sys.argv[i]).lower()
                if sr == "from":
                    i -= 1
                    break
                else:
                    to += sr + " "
                    i += 1

        if ukaz == "from":
            i += 1
            while i < len(sys.argv):
                sr = str(sys.argv[i]).lower()
                if sr == "to":
                    i -= 1
                    break
                else:
                    from_loc += sr + " "
                    i += 1

        i += 1

    if from_loc == "":
        try:
            with open('_data.txt', encoding="utf-8") as json_data:
                d = json.load(json_data)
                from_loc = d["station_name"]
        except FileNotFoundError:
            print("Specify the departure location.")
            print("from (departure) to (destination)")
            exit(1)

    if to == "" and from_loc == "":
        print("Unknown arguments. Try:")
        print("from (departure) to (destination)")
        exit()

    route_to_from(from_loc, to)
    write_data(from_loc)


else:
    print("No arguments. Try:")
    print("from (departure) to (destination)")
    exit()
