exec(open('.local/cm01-qa-harness/backend43.py').read().split('# T01/T02')[0])
markers=out/('fault-'+runid);markers.mkdir();pgcase='capmoney-cm43-qa-fault-pg-'+runid;sink='capmoney-cm43-qa-smtp-'+runid;db='cm43fault'
pgimage=d('inspect','--format','{{.Image}}',pg)
d('run','-d','--name',pgcase,'--network','capmoney-mvc-qa_data_net','--network-alias',pgcase,'-e','POSTGRES_USER=capmoney_qa','-e','POSTGRES_DB='+db,'-e','POSTGRES_PASSWORD_FILE=/run/secrets/pg_password','--mount','type=bind,src='+str(secret/'pg-password')+',dst=/run/secrets/pg_password,readonly','--mount','type=volume,src='+pgcase+'-data,dst=/var/lib/postgresql/data',pgimage)
for _ in range(60):
 try:
  if d('exec',pgcase,'pg_isready','-U','capmoney_qa','-d',db):break
 except subprocess.CalledProcessError:pass
 time.sleep(.2)
d('run','-d','--name',sink,'--network','capmoney-mvc-qa_mail_net','--network-alias',sink,'--mount','type=bind,src='+str(markers.resolve())+',dst=/markers','--mount','type=bind,src='+str(pathlib.Path('.local/cm01-qa-harness/smtp-accepted-db-loss.py').resolve())+',dst=/sink.py,readonly','python:3.13-alpine','python','/sink.py','/markers')
name=create('db-loss',db,smtp=sink,dbhost=pgcase);start(name);stopped=False
try:
 for _ in range(3000):
  if (markers/'smtp-before-accept').exists():break
  time.sleep(.02)
 else:raise AssertionError('SMTP DATA not reached')
 started=time.monotonic();d('stop','--time','0',pgcase);stopped=True;(markers/'db-stopped').write_text('stopped')
 res=finish(name);assert (markers/'smtp-accepted').exists(),'SMTP250 never sent';assert res['code']=='unknown' and res['exit']==3,res
 elapsed=round(time.monotonic()-started,3)
finally:
 if stopped:d('start',pgcase)
for _ in range(60):
 try:
  raw=d('exec',pgcase,'psql','-U','capmoney_qa','-d',db,'-At','-c',"SELECT json_build_object('generation',generation,'state',state,'accepted_at',accepted_at,'expires_at',expires_at) FROM manager_bootstrap");row=json.loads(raw);break
 except (subprocess.CalledProcessError,json.JSONDecodeError):time.sleep(.2)
assert row['state']!='issued' and row['accepted_at'] is None
# Restore/start original mailpit route and allow stale sending lease to expire.
time.sleep(31);retryname=create('db-loss-retry',db,dbhost=pgcase);start(retryname);retry=finish(retryname);assert retry['code']=='accepted' and retry['generation']>row['generation']
result=dict(commit=a.sha,case='smtp250-after-real-db-stop',postgres=pgcase,smtp=sink,cli=res,state_after_restore=row,retry=retry,elapsed_after_stop=elapsed,all_resources_retained=True)
(out/'db-loss43.json').write_text(json.dumps(result,indent=2));print('Actual SMTP accepted DB-loss and retry passed; sanitized result saved')
