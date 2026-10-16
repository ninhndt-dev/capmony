"""Serve a built Vite app with a fallback for React Router browser tests."""

from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
import sys


root = Path(sys.argv[1]).resolve()
port = int(sys.argv[2])


class Handler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(root), **kwargs)

    def do_GET(self):
        if not (root / self.path.lstrip("/")).exists() and not self.path.startswith("/api/"):
            self.path = "/index.html"
        return super().do_GET()


ThreadingHTTPServer(("127.0.0.1", port), Handler).serve_forever()
