#!/usr/bin/env python3
"""Check bounded health failure for a silent DB; pause/unpause preserves resources."""
import argparse
import json
import subprocess
import time
import urllib.error
import urllib.request

parser = argparse.ArgumentParser()
parser.add_argument('--origin', default='http://127.0.0.1:18080')
parser.add_argument('--database-container', default='capmoney-dev-postgres-1')
parser.add_argument('--api-container', default='capmoney-dev-api-1')
args = parser.parse_args()

def docker(*command):
    return subprocess.check_output(['docker', *command], text=True).strip()

def identity():
    state = json.loads(docker('inspect', '--format', '{{json .State}}', args.api_container))
    return {key: state[key] for key in ['Pid', 'StartedAt']}

def get(path):
    started = time.monotonic()
    try:
        with urllib.request.urlopen(args.origin + path, timeout=5) as response:
            status = response.status
    except urllib.error.HTTPError as response:
        status = response.code
    return status, round(time.monotonic() - started, 3)

assert get('/api/health')[0] == 200, 'API must be healthy before the test'
before = identity()
observations = {}
docker('pause', args.database_container)
try:
    for path in ['/api/health', '/api/ready/auth']:
        status, elapsed = get(path)
        observations[path] = {'status': status, 'seconds': elapsed}
        assert status == 503 and elapsed < 5, observations
    assert identity() == before, 'JVM restarted during silent DB outage'
finally:
    docker('unpause', args.database_container)
for attempt in range(30):
    if get('/api/ready/auth')[0] == 200:
        break
    time.sleep(1)
else:
    raise AssertionError('Readiness did not recover after unpause')
assert identity() == before, 'JVM restarted during recovery'
print(json.dumps({'identity': before, 'paused_database': observations,
                  'recovered': True}, indent=2))
