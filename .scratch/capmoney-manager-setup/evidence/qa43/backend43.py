"""Independent real CLI cases; execute only with reviewed --sha/--image after DEV_REVIEWED.
Use retained capmoney-mvc-qa dependency networks; creates new databases and CLI containers.
No token or mail body is printed/saved. Fault clock/CAS seams are covered by fresh JUnit.
"""
import argparse,subprocess,os,json,pathlib,time,hashlib,re,uuid
p=argparse.ArgumentParser();p.add_argument('--sha',required=True);p.add_argument('--image',required=True);a=p.parse_args()
assert subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip()==a.sha
os.environ.update(DOCKER_CONFIG=str(pathlib.Path('.local/docker-cli').resolve()),DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
out=pathlib.Path('.local/cm01-qa');out.mkdir(exist_ok=True);runid=time.strftime('%Y%m%d%H%M%S');pg='capmoney-mvc-qa-postgres-1';secret=pathlib.Path('.local/qa-mvc-final/secrets').resolve();results=[]
def d(*args):return subprocess.check_output(['docker',*args],text=True).strip()
def sql(db,q):return d('exec',pg,'psql','-U','capmoney_qa','-d',db,'-At','-v','ON_ERROR_STOP=1','-c',q)
def database(case):
 db='cm01qa_'+case+'_'+runid;d('exec',pg,'createdb','-U','capmoney_qa',db);return db
def create(case,db,email='manager@example.test',smtp='mailpit',port='1025',dbhost='postgres',enabled='true'):
 name='capmoney-cm01-qa-'+case+'-'+runid
 args=['create','--name',name,'--network','capmoney-mvc-qa_api_net']
 env=dict(APP_ORIGIN='http://127.0.0.1:15173',API_SUBNET='172.30.61.0/24',EDGE_SECRET_FILE='/run/secrets/edge_auth',DB_HOST=dbhost,DB_NAME=db,DB_USER='capmoney_qa',REDIS_HOST='redis',SMTP_HOST=smtp,SMTP_PORT=port,BOOTSTRAP_ENABLED=enabled,MANAGER_EMAIL=email)
 for k,v in env.items():args+=['-e',k+'='+v]
 for src,dest in [('edge-auth','edge_auth'),('pg-password','pg_password')]:args+=['--mount','type=bind,src='+str(secret/src)+',dst=/run/secrets/'+dest+',readonly']
 args+=[a.image,'--capmoney.bootstrap-manager'];d(*args)
 for net in ['data','rate','mail']:d('network','connect','capmoney-mvc-qa_'+net+'_net',name)
 return name
def start(name):d('start',name)
def finish(name):
 code=int(d('wait',name));logs=d('logs',name);m=re.findall(r'bootstrap=([a-z_]+) generation=(\d+)',logs)
 assert m,'CLI final state absent';assert '#token=' not in logs and 'manager@example.test' not in logs
 return dict(container=name,exit=code,code=m[-1][0],generation=int(m[-1][1]))
def state(db):
 return sql(db,"SELECT json_build_object('generation',generation,'state',state,'digest_hex',token_digest ~ '^[0-9a-f]{64}$','accepted_at',accepted_at,'expires_at',expires_at,'duration72h',expires_at=accepted_at+interval '72 hours') FROM manager_bootstrap")
# T01/T02 real CLI acceptance; no HTTP bootstrap is covered separately by browser/runtime.
db=database('accept');name=create('accept',db);start(name);res=finish(name);assert res['code']=='accepted' and res['exit']==0
row=json.loads(state(db));assert row['state']=='issued' and row['digest_hex'] and row['duration72h'];results.append(dict(case='real-cli-accepted',database=db,result=res,state=row))
# Mailpit inbox is separate proof from SMTP accepted. Keep actual token only in memory.
mailjson=json.loads(d('run','--name','capmoney-cm01-qa-mail-list-'+runid,'--network','capmoney-mvc-qa_mail_net','curlimages/curl:8.16.0','-sS','http://mailpit:8025/api/v1/messages'))
messages=mailjson.get('messages',mailjson.get('Messages',[]));assert messages,'Mailpit inbox empty'
found=None
for index,msg in enumerate(messages):
 ident=msg.get('ID',msg.get('id'))
 detail=json.loads(d('run','--name','capmoney-cm01-qa-mail-read-'+str(index)+'-'+runid,'--network','capmoney-mvc-qa_mail_net','curlimages/curl:8.16.0','-sS','http://mailpit:8025/api/v1/message/'+ident))
 text=detail.get('Text',detail.get('text',''));match=re.search(r'#token=([A-Za-z0-9_-]{43})',text)
 if not match:continue
 token=match.group(1);digest=hashlib.sha256(token.encode()).hexdigest()
 if sql(db,'SELECT token_digest FROM manager_bootstrap')==digest:
  assert 'manager@example.test' in json.dumps(detail.get('To',detail.get('to',[])))
  assert token not in d('logs',name)
  found=dict(mail_id=ident,recipient='manager@example.test',token_32_bytes=True,fragment_only=True,digest_matches_db=True,token_absent_cli_log=True);break
assert found,'No matching issued token in correct mailbox'
results.append(dict(case='mailpit-fragment-digest',evidence=found))

# A second CLI cannot issue a second live link; mismatched destination is fail closed.
for case,email in [('alreadyissued','manager@example.test'),('emailmismatch','other@example.test')]:
 name=create(case,db,email=email);start(name);res=finish(name);assert res['code']=='unavailable' and res['exit']==2;assert json.loads(state(db))==row;results.append(dict(case=case,result=res,state_unchanged=True))
# Real connection refusal before SMTP acceptance; safe retry receives new generation.
db=database('refused');name=create('refused',db,smtp='127.0.0.1',port='1');start(name);res=finish(name);assert res['code']=='failed';old=json.loads(state(db));assert old['state']=='failed'
name=create('retry',db);start(name);retry=finish(name);new=json.loads(state(db));assert retry['code']=='accepted' and new['generation']==old['generation']+1;results.append(dict(case='refused-retry',database=db,refused=res,retry=retry,state=new))
# Two OS processes/JVM race; DB serializes at most one issued generation.
db=database('race');names=[create('race'+str(i),db) for i in range(2)]
for n in names:start(n)
race=[finish(n) for n in names];assert sum(x['code']=='accepted' for x in race)==1;row=json.loads(state(db));assert row['state']=='issued';results.append(dict(case='two-cli-processes',database=db,results=race,state=row))
# Already-manager fixture is test-only SQL, not product schema delivered by #43.
db=database('manager');name=create('disabled',db,enabled='false');start(name);disabled=finish(name);assert disabled['code']=='disabled'
sql(db,'CREATE TABLE manager_account(id bigint PRIMARY KEY); INSERT INTO manager_account VALUES (1)')
name=create('managerexists',db);start(name);existing=finish(name);assert existing['code']=='unavailable';assert json.loads(state(db))['generation']==0
results.append(dict(case='existing-manager-test-fixture',database=db,result=existing,generation_unchanged=True))
(out/'backend43.json').write_text(json.dumps(dict(commit=a.sha,image=a.image,results=results),indent=2));print('Independent CLI cases passed; sanitized result saved; all resources retained')
