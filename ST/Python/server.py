"""An example of a simple HTTP server."""
from __future__ import print_function

import mimetypes
import pickle
import socket
from os.path import isdir

try:
    from urllib.parse import unquote_plus
except ImportError:
    from urllib import unquote_plus

# Pickle file for storing data
PICKLE_DB = "db.pkl"

# Directory containing www data
WWW_DATA = "www-data"

# Header template for a successful HTTP request
HEADER_RESPONSE_200 = """HTTP/1.1 200 OK
content-type: %s
content-length: %d
connection: Close

"""

# Represents a table row that holds user data
TABLE_ROW = """
<tr>
    <td>%d</td>
    <td>%s</td>
    <td>%s</td>
</tr>
"""

# Template for a 404 (Not found) error
RESPONSE_404 = """HTTP/1.1 404 Not found
content-type: text/html
connection: Close

<!doctype html>
<h1>404 Page not found</h1>
<p>Page cannot be found.</p>
"""

# Template for a 400 Bad request error
RESPONSE_400 = """HTTP/1.1 400 Bad request
content-type: text/html
connection: Close

<!doctype html>
<h1>400 Bad request</h1>
<p>%s</p>
"""

# Template for a 301 (Moved Permanently) error
RESPONSE_301 = """HTTP/1.1 301 Moved Permanently
location: http://localhost:%d%sindex.html

"""


def save_to_db(first, last):
    """Create a new user with given first and last name and store it into
    file-based database.

    For instance, save_to_db("Mick", "Jagger"), will create a new user
    "Mick Jagger" and also assign him a unique number.

    Do not modify this method."""

    existing = read_from_db()
    existing.append({
        "number": 1 if len(existing) == 0 else existing[-1]["number"] + 1,
        "first": first,
        "last": last
    })
    with open(PICKLE_DB, "wb") as handle:
        pickle.dump(existing, handle)


def read_from_db(criteria=None):
    """Read entries from the file-based DB subject to provided criteria

    Use this method to get users from the DB. The criteria parameters should
    either be omitted (returns all users) or be a dict that represents a query
    filter. For instance:
    - read_from_db({"number": 1}) will return a list of users with number 1
    - read_from_db({"first": "bob"}) will return a list of users whose first
    name is "bob".

    Do not modify this method."""
    if criteria is None:
        criteria = {}
    else:
        # remove empty criteria values
        for key in ("number", "first", "last"):
            if key in criteria and criteria[key] == "":
                del criteria[key]

        # cast number to int
        if "number" in criteria:
            criteria["number"] = int(criteria["number"])

    try:
        with open(PICKLE_DB, "rb") as handle:
            data = pickle.load(handle)

        filtered = []
        for entry in data:
            predicate = True

            for key, val in criteria.items():
                if val != entry[key]:
                    predicate = False

            if predicate:
                filtered.append(entry)

        return filtered
    except (IOError, EOFError):
        return []


def parse_headers(client):
    headers = {}
    while True:
        line = client.readline().decode("utf-8").strip()

        if line == "":
            return headers

        key, value = line.split(":", 1)
        headers[key.lower().strip()] = value.strip()


def write_error_400(client, msg):
    response = RESPONSE_400 % msg
    client.write(response.encode("utf-8"))
    client.close()
    return


def write_error_301(client, port, uri):
    response = RESPONSE_301 % (port, uri)
    client.write(response.encode("utf-8"))
    client.close()
    return


def create_rows(students):
    rows = ""
    for r in students:
        rows += TABLE_ROW % (r["number"], r["first"], r["last"])
    return rows


def write_app_list(client, students):
    try:
        uri = WWW_DATA + "/app_list.html"
        with open(uri, "r") as handel:
            response_body = handel.read()

        mime_type, _ = mimetypes.guess_type(uri)
        if mime_type is None:
            mime_type = "application/octet-stream"
        rows = create_rows(students)
        response_body = response_body.replace("{{students}}", rows)
        response_header = HEADER_RESPONSE_200 % (mime_type, len(response_body))

        client.write(response_header.encode("utf-8"))
        client.write(response_body.encode("utf-8"))
        client.close()
    except IOError:
        client.write(RESPONSE_404.encode("utf-8"))


def process_request(connection, address, listen_port):
    """Process an incoming socket request.

    :param listen_port:
    :param connection is a socket of the client
    :param address is a 2-tuple (address(str), port(int)) of the client
    """

    address, port = address

    # Make reading from a socket like reading/writing from a file
    # Use binary mode, so we can read and write binary data. However,
    # this also means that we have to decode (and encode) data (preferably
    # to utf-8) when reading (and writing) text
    client = connection.makefile("wrb")

    # Read one request_line, decode it to utf-8 and strip leading and trailing spaces
    request_line = client.readline().decode("utf-8").strip()
    headers = {}
    try:
        verb, uri, version = request_line.split(" ")
        if not (verb == "GET" or verb == "POST"):
            write_error_400(client, "Only GET and POST requests are supported")
            return
        assert uri[0] == "/", write_error_400(client, "Invalid uri")
        assert version == "HTTP/1.1", write_error_400(client, "Wrong HTTP version")

        headers = parse_headers(client)

    except Exception as e:
        write_error_400(client, "Wrong format of header")
        return

    if "host" in headers:
        v = headers["host"]
        if listen_port == 80:
            h = "localhost"
        else:
            h = "localhost:%d" % listen_port

        if v != h:
            write_error_400(client, "Wrong host in headers")
            return
    else:
        write_error_400(client, "Missing host in headers")
        return

    # print(headers)
    print("[%s:%d] REQUEST: %s %s %s" % (address, port, verb, uri, version))

    # URL konča s / PREUSMERI
    if uri.endswith("/"):
        write_error_301(client, listen_port, uri)
        return

    # uri = "index.html" if uri == "/" else uri[1:]

    if uri == "/app-add" and verb == "POST":
        body = str(client.read(int(headers["content-length"])).decode("utf-8"))
        print(body)
        body = unquote_plus(body)
        body = body.split("&")
        args = {}
        for x in body:
            k, v = x.split("=")
            args[k] = v
        if "first" in args and "last" in args:
            save_to_db(args["first"], args["last"])
            uri = "/app_add.html"
        else:
            write_error_400(client, "Missing arguments")
            return
    elif uri == "/app-add":
        write_error_400(client, "app-add only works with POST request.")
        return
    elif str(uri).startswith("/app-index") and verb == "GET":
        body = uri.split("?", 1)
        args = {}
        if len(body) == 2:
            body = body[1]
            body = unquote_plus(body)
            body = body.split("&")
            for x in body:
                k, v = x.split("=")
                args[k] = v

        if "first" in args or "last" in args or "number" in args:
            students = read_from_db(args)
        else:
            students = read_from_db()

        write_app_list(client, students)
        return
    elif uri == "/app-index":
        write_error_400(client, "app-index only works with GET request.")
        return

    uri = WWW_DATA + uri
    if isdir(uri):
        uri = "/" + uri.split("/", 1)[1]
        write_error_301(client, listen_port, uri + "/")
        return

    try:
        with open(uri, "rb") as handel:
            response_body = handel.read()

        mime_type, _ = mimetypes.guess_type(uri)
        if mime_type is None:
            mime_type = "application/octet-stream"

        response_header = HEADER_RESPONSE_200 % (mime_type, len(response_body))

        client.write(response_header.encode("utf-8"))
        client.write(response_body)

    except IOError:
        client.write(RESPONSE_404.encode("utf-8"))

    # Closes file-like object
    client.close()


def main(port):
    """Starts the server and waits for connections."""

    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind(("", port))
    server.listen(1)

    print("Listening on %d" % port)

    while True:
        connection, address = server.accept()
        print("[%s:%d] CONNECTED" % address)
        process_request(connection, address, port)
        connection.close()
        print("[%s:%d] DISCONNECTED" % address)


if __name__ == "__main__":
    save_to_db("Simon", "Korošec")
    save_to_db("Janko", "Nekaj")
    save_to_db("To ", "Sm Js")
    main(8080)
