import os,subprocess,json,pathlib,secrets
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
root=pathlib.Path('.local/qa-java-final-v2');root.mkdir(exist_ok=True)
s=root/'secrets';s.mkdir(exist_ok=True);s.chmod(0o700)
for n in ['pg-password','edge-auth']:
 p=s/n
 if not p.exists():p.write_text(secrets.token_hex(32))
 p.chmod(0o600)
os.environ.update(CAPMONEY_SECRET_DIR=str(s.resolve()),CAPMONEY_PORT='48081',APP_ORIGIN='http://127.0.0.1:48081',API_SUBNET='172.30.53.0/24',DB_NAME='capmoney_qa',DB_USER='capmoney_qa',REDIS_DATABASE='2')
def run(*a):return subprocess.check_output(a,text=True).strip()
for project in ['capmoney-java-qa','capmoney-java-qa-probe']:
 subprocess.run(['docker','compose','-p',project,'-f','infra/compose.yaml','stop'],check=True)
over=root/'reuse.yaml';over.write_text('services:\n  api:\n    image: capmoney-dev-api\n  web:\n    image: capmoney-dev-web\n')
subprocess.run(['docker','compose','-p','capmoney-java-qa-v2','-f','infra/compose.yaml','up','--build','--no-recreate','-d'],check=True)
os.environ.update(CAPMONEY_PORT='58081',APP_ORIGIN='http://127.0.0.1:58081',API_SUBNET='172.30.54.0/24')
subprocess.run(['docker','compose','-p','capmoney-java-qa-v2-probe','-f','infra/compose.yaml','-f','infra/compose.probe.yaml','up','--build','--no-recreate','-d'],check=True)
