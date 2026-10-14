import os,subprocess,json,pathlib,secrets
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
root=pathlib.Path('.local/cm43-runtime');root.mkdir(exist_ok=True)
s=root/'secrets';s.mkdir(exist_ok=True);s.chmod(0o700)
for n in ['pg-password','edge-auth']:
 p=s/n
 if not p.exists():p.write_text(secrets.token_hex(32))
 p.chmod(0o600)
os.environ.update(CAPMONEY_SECRET_DIR=str(s.resolve()),CAPMONEY_PORT='18083',APP_ORIGIN='http://127.0.0.1:18083',API_SUBNET='172.30.91.0/24',DB_NAME='capmoney_qa',DB_USER='capmoney_qa',REDIS_DATABASE='2')
def run(*a):return subprocess.check_output(a,text=True).strip()
# Build immutable images separately with preservation; Compose never builds.
runtime=root/'runtime.yaml';runtime.write_text('services:\n  api:\n    image: capmoney-cm43-api:f63efe4\n  web:\n    image: capmoney-cm43-web:f63efe4\n  mailcheck:\n    image: capmoney-mvc-tools:5a4a130\n')
probe=root/'probe.yaml';probe.write_text('services:\n  api:\n    image: capmoney-mvc-probe:5a4a130\n  web:\n    image: capmoney-cm43-web:f63efe4\n')
for overlay,base in [(runtime,90)]:
 with overlay.open('a') as f:
  f.write('networks:\n')
  for offset,name in enumerate(['web_net','data_net','rate_net','mail_net']):
   f.write('  '+name+':\n    ipam:\n      config:\n        - subnet: 10.247.'+str(base+offset)+'.0/24\n')

# config loader boundary used by other harnesses
for project in []:pass
subprocess.run(['docker','compose','-p','capmoney-cm43-qa','-f','infra/compose.yaml','-f',str(runtime),'up','--no-build','--no-recreate','-d'],check=True)
