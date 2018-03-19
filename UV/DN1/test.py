import requests

od = "Živalski vrt"
do = "Kolodvor"
api_key = "AIzaSyBsPC63YFK5z05Ab2krX1rM_Ry-KTaEdyc"
#r = requests.get("https://maps.googleapis.com/maps/api/directions/json?origin=" + od + "&destination=" + do + "&mode=transit&alternatives=true&key=" + api_key + "")
r = requests.get("https://maps.googleapis.com/maps/api/directions/json?origin=Živalski vrt&destination=kolodvor&mode=transit&alternatives=true&key=AIzaSyBsPC63YFK5z05Ab2krX1rM_Ry-KTaEdyc")
routes = r.json()["routes"]

i = 0
j = 0

for ro in routes:
    naj = int(str(routes[i]["legs"][0]["duration"]["text"]).split(" ")[0])
    t = int(str(ro["legs"][0]["duration"]["text"]).split(" ")[0])

    if t < naj:
        i = j

    j += 1

eta = routes[i]["legs"][0]["duration"]["text"]  # dolžina poti

for step in routes[i]["legs"][0]["steps"]:
    if step["travel_mode"] == "TRANSIT":
        direction = step["transit_details"]["headsign"]  # smer
        izstop = step["transit_details"]["arrival_stop"]["name"]  # izstop če je bus
        line_num = step["transit_details"]["line"]["short_name"]  # št trole
        print("Take the bus line {} towards {} and get off at {}.".format(line_num, direction, izstop))
    elif step["travel_mode"] == "WALKING":
        navodila = step["html_instructions"]  # navodila
        print(navodila + ".")

print("Estimated time of journey " + eta + ".")
