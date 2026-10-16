import http.client
import os, subprocess, json, time, urllib.request, urllib.error
os.environ['DOCKER_CONFIG']=os.getcwd()+'/.local/docker-cli'
os.environ['DOCKER_HOST']='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock'
results={}
def docker(*args): return subprocess.check_output(['docker',*args],text=True).strip()
def get(path):
    try:
        with urllib.request.urlopen('http://127.0.0.1:18082'+path,timeout=5) as r: return [r.status,r.read().decode()]
    except urllib.error.HTTPError as e: return [e.code,e.read().decode()]
def identity():
    return json.loads(docker('inspect','--format','{{json .State}}','capmoney-mvc-qa-api-1'))
def waitup():
    for _ in range(30):
        try:
            if get('/api/ready/auth')[0]==200: return
        except (urllib.error.URLError, ConnectionError, TimeoutError, http.client.RemoteDisconnected): pass
        time.sleep(1)
    raise AssertionError('readiness did not recover')
waitup()
base=identity()
results['before']={k:base[k] for k in ['Pid','StartedAt']}
results['routes']={p:get(p) for p in ['/','/manager/setup','/api/health','/api/ready/auth','/api/__probe','/v3/api-docs']}
# Keep only status for static HTML/docs.
for p in ['/','/manager/setup','/v3/api-docs']: results['routes'][p]=results['routes'][p][0]
for dependency in ['postgres','redis']:
    name='capmoney-mvc-qa-'+dependency+'-1'
    try:
        docker('stop',name)
        results[dependency+'_outage']={p:get(p) for p in ['/api/health','/api/ready/auth']}
        results[dependency+'_outage_repeated']=[{p:get(p) for p in ['/api/health','/api/ready/auth']} for _ in range(3)]
        expected=[503,503] if dependency=='postgres' else [200,503]
        assert [v[0] for v in results[dependency+'_outage'].values()]==expected
        state=identity()
        assert state['Pid']==base['Pid'] and state['StartedAt']==base['StartedAt']
    finally:
        docker('start',name)
    waitup()
    state=identity()
    assert state['Pid']==base['Pid'] and state['StartedAt']==base['StartedAt']
    results[dependency+'_recovery']='UP; same PID and StartedAt'
results['images']=json.loads(docker('inspect','--format','{{json .Image}}','capmoney-mvc-qa-api-1'))
results['containers']=json.loads(docker('inspect','--format','{{json .NetworkSettings.Ports}}','capmoney-mvc-qa-caddy-1'))
results['runtime_probe_class_absent']=docker('exec','capmoney-mvc-qa-api-1','sh','-c',"! unzip -l /app/app.jar | grep -q ProbeController") if False else 'not checked'
with open('.local/qa-mvc-final/runtime.json','w') as f: json.dump(results,f,indent=2)
print(json.dumps(results,indent=2))
