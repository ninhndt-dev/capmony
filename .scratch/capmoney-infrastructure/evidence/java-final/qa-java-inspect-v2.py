import os,subprocess,json,pathlib,urllib.request,urllib.error
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
r=pathlib.Path('.local/qa-java-final-v2')
def d(*a):return subprocess.check_output(['docker',*a],text=True)
cs=json.loads(d('inspect',*[f'capmoney-java-qa-v2-{s}-1' for s in ['caddy','web','api','postgres','redis','mailpit']]))
res={'containers':[{ 'name':c['Name'],'image':c['Image'],'ports':c['HostConfig']['PortBindings'],'networks':list(c['NetworkSettings']['Networks'])} for c in cs]}
for c in cs:
 if 'caddy' in c['Name']:assert c['HostConfig']['PortBindings']['8080/tcp'][0]['HostIp']=='127.0.0.1'
 else:assert not c['HostConfig']['PortBindings']
res['networks']=json.loads(d('network','inspect',*[f'capmoney-java-qa-v2_{n}_net' for n in ['web','api','data','rate','mail']]))
subprocess.run(['docker','cp','capmoney-java-qa-v2-api-1:/app/app.jar',str(r/'runtime.jar')],check=True)
import zipfile
with zipfile.ZipFile(r/'runtime.jar') as z:
 names=z.namelist();assert not any('ProbeController' in n or 'MailpitMailTest' in n or 'test-classes' in n for n in names)
 res['runtime_test_classes_absent']=True
secretvalues=[p.read_bytes() for p in (r/'secrets').iterdir()]
logs=''.join(d('logs',c['Name']) for c in cs)
config=d('inspect',*[c['Name'] for c in cs])
res['secret_values_absent_container_config_and_logs']=all(v.decode() not in logs+config for v in secretvalues)
res['caddy_adapt_exit']=subprocess.run(['docker','exec','capmoney-java-qa-v2-caddy-1','caddy','adapt','--config','/etc/caddy/Caddyfile'],stdout=subprocess.DEVNULL).returncode
res['secret_files']=[{'name':p.name,'bytes':p.stat().st_size,'mode':oct(p.stat().st_mode&0o777)} for p in (r/'secrets').iterdir()]
(r/'inspection.json').write_text(json.dumps(res,indent=2))
print('Topology ports, runtime JAR exclusion, secret scan and Caddy adapt checked; secret data omitted')
