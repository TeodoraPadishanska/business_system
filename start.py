from http.server import HTTPServer, SimpleHTTPRequestHandler

server = HTTPServer(("0.0.0.0", 8000), SimpleHTTPRequestHandler)
print("Serving on http://localhost:8000")
server.serve_forever()