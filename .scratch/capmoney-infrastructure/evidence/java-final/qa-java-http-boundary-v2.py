import urllib.request,urllib.error,socket,json,pathlib
res={}
for p in ['/api/health','/api/ready/auth']:
 with urllib.request.urlopen('http://127.0.0.1:48081'+p) as r:
  assert r.headers.get('Cache-Control')=='no-store';res[p]=dict(status=r.status,cache_control=r.headers.get('Cache-Control'),body=r.read().decode())
for port in [3000,5432,6379,1025,8025]:
 s=socket.socket();s.settimeout(1)
 try:s.connect(('127.0.0.1',port));res[str(port)]='connectable';raise AssertionError('direct service port open:'+str(port))
 except (ConnectionRefusedError,TimeoutError):res[str(port)]='unreachable'
 finally:s.close()
pathlib.Path('.local/qa-java-final-v2/http-boundary.json').write_text(json.dumps(res,indent=2));print(res)
