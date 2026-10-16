import os,subprocess,json,time,pathlib
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
r=pathlib.Path('.local/qa-java-final-v2');name='capmoney-java-qa-v2-api-initial';pg='capmoney-java-qa-v2-postgres-1'
def d(*a):return subprocess.check_output(['docker',*a],text=True).strip()
api=json.loads(d('inspect','capmoney-java-qa-v2-api-1'))[0];args=['create','--name',name,'--restart','unless-stopped','--network','capmoney-java-qa-v2_api_net']
for e in api['Config']['Env']:
 if e.split('=')[0] in ['APP_ORIGIN','API_SUBNET','EDGE_SECRET_FILE','DB_HOST','DB_NAME','DB_USER','REDIS_HOST','REDIS_DATABASE','SMTP_HOST']:args+=['-e',e]
for source,target in [('edge-auth','edge_auth'),('pg-password','pg_password')]:args+=['--mount','type=bind,src='+str((r/'secrets'/source).resolve())+',dst=/run/secrets/'+target+',readonly']
args+=[api['Image']];d(*args)
for net in ['data','rate','mail']:d('network','connect','capmoney-java-qa-v2_'+net+'_net',name)
d('stop',pg)
try:
 d('start',name)
 for _ in range(45):
  c=json.loads(d('inspect',name))[0]
  if c['RestartCount']>0:break
  time.sleep(1)
 else:raise AssertionError('No retry after initial PG unavailable')
 unavailable={'restart_count':c['RestartCount'],'status':c['State']['Status']}
finally:d('start',pg)
for _ in range(60):
 logs=d('logs',name)
 if 'Started ApiApplication' in logs:break
 time.sleep(1)
else:raise AssertionError('Initial retry did not start after PG recovered')
res={'initial_pg_unavailable':unavailable,'recovered_after_pg_started':True,'container':name};(r/'initial-startup.json').write_text(json.dumps(res,indent=2));print(res)
