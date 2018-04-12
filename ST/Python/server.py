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
        headers[key.strip()] = value.strip()


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
        assert verb == "GET", write_error_400(client, "Only GET requests are supported")
        assert uri[0] == "/", write_error_400(client, "Invalid uri")
        assert version == "HTTP/1.1", write_error_400(client, "Wrong HTTP version")

        headers = parse_headers(client)

    except Exception as e:
        write_error_400(client, "Wrong format of header")
        return

    print("[%s:%d] REQUEST: %s %s %s" % (address, port, verb, uri, version))

    # URL konča s / PREUSMERI
    if uri.endswith("/"):
        write_error_301(client, listen_port, uri)
        return

    # uri = "index.html" if uri == "/" else uri[1:]
    uri = WWW_DATA + uri
    print(uri)
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

    # Read and parse the request line

    # Read and parse headers

    # Read and parse the body of the request (if applicable)

    # create the response

    # Write the response back to the socket


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
    main(8080)
