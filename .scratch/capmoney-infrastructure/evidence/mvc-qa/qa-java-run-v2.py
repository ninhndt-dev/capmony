import os,subprocess,json,pathlib,secrets
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
root=pathlib.Path('.local/qa-mvc-final');root.mkdir(exist_ok=True)
s=root/'secrets';s.mkdir(exist_ok=True);s.chmod(0o700)
for n in ['pg-password','edge-auth']:
 p=s/n
 if not p.exists():p.write_text(secrets.token_hex(32))
 p.chmod(0o600)
os.environ.update(CAPMONEY_SECRET_DIR=str(s.resolve()),CAPMONEY_PORT='18082',APP_ORIGIN='http://127.0.0.1:18082',API_SUBNET='172.30.61.0/24',DB_NAME='capmoney_qa',DB_USER='capmoney_qa',REDIS_DATABASE='2')
def run(*a):return subprocess.check_output(a,text=True).strip()
# Build immutable images separately with preservation; Compose never builds.
runtime=root/'runtime.yaml';runtime.write_text('services:\n  api:\n    image: capmoney-mvc-api:5a4a130\n  web:\n    image: capmoney-dev-web\n  mailcheck:\n    image: capmoney-mvc-tools:5a4a130\n')
probe=root/'probe.yaml';probe.write_text('services:\n  api:\n    image: capmoney-mvc-probe:5a4a130\n  web:\n    image: capmoney-dev-web\n')
for overlay,base in [(runtime,70),(probe,80)]:
 with overlay.open('a') as f:
  f.write('networks:\n')
  for offset,name in enumerate(['web_net','data_net','rate_net','mail_net']):
   f.write('  '+name+':\n    ipam:\n      config:\n        - subnet: 10.247.'+str(base+offset)+'.0/24\n')

# config loader boundary used by other harnesses
for project in []:pass
subprocess.run(['docker','compose','-p','capmoney-mvc-qa','-f','infra/compose.yaml','-f',str(runtime),'up','--no-build','--no-recreate','-d'],check=True)
os.environ.update(CAPMONEY_PORT='28082',APP_ORIGIN='http://127.0.0.1:28082',API_SUBNET='172.30.62.0/24')
subprocess.run(['docker','compose','-p','capmoney-mvc-qa-probe','-f','infra/compose.yaml','-f','infra/compose.probe.yaml','-f',str(probe),'up','--no-build','--no-recreate','-d'],check=True)
