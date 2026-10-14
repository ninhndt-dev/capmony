import os,subprocess,json,time,pathlib
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
r=pathlib.Path('.local/qa-java-final-v2');res={}
def d(*a):return subprocess.check_output(['docker',*a],text=True).strip()
def sql(c,q):return d('exec',c,'sh','-c','psql -v ON_ERROR_STOP=1 -At -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "$1"','sh',q)
def wait(c):
 for _ in range(40):
  try:return sql(c,'SELECT 1')
  except subprocess.CalledProcessError:time.sleep(1)
 raise AssertionError('database not ready')
old='capmoney-java-qa-v2-postgres-1';dev='capmoney-dev-postgres-1'
for c in [old,dev]:sql(c,"CREATE TABLE IF NOT EXISTS qa_infra_marker (value text PRIMARY KEY); INSERT INTO qa_infra_marker VALUES ('qa_310ad7f') ON CONFLICT DO NOTHING")
before=sql(dev,'SELECT value FROM qa_infra_marker ORDER BY value')
d('restart',old);wait(old);assert sql(old,'SELECT value FROM qa_infra_marker')=='qa_310ad7f';res['restart_marker']=True
mounts=json.loads(d('inspect','--format','{{json .Mounts}}',old));volume=next(m['Name'] for m in mounts if m['Destination']=='/var/lib/postgresql/data')
image=d('inspect','--format','{{.Image}}',old);secret=str((r/'secrets/pg-password').resolve())
def create(name,vol):
 return d('run','-d','--name',name,'--network','capmoney-java-qa-v2_data_net','-e','POSTGRES_DB=capmoney_qa','-e','POSTGRES_USER=capmoney_qa','-e','POSTGRES_PASSWORD_FILE=/run/secrets/pg_password','--mount','type=bind,src='+secret+',dst=/run/secrets/pg_password,readonly','--mount','type=volume,src='+vol+',dst=/var/lib/postgresql/data',image)
successor='capmoney-java-qa-v2-pg-successor'
d('stop',old)
try:
 create(successor,volume);wait(successor);assert sql(successor,'SELECT value FROM qa_infra_marker')=='qa_310ad7f';res['successor_same_volume_marker']=True
finally:
 d('stop',successor);d('start',old);wait(old)
fresh='capmoney-java-qa-v2-pg-fresh';create(fresh,'capmoney-java-qa-v2-fresh-data');wait(fresh)
assert sql(fresh,"SELECT to_regclass('public.qa_infra_marker') IS NULL")=='t'
assert sql(old,'SELECT value FROM qa_infra_marker')=='qa_310ad7f';assert sql(dev,'SELECT value FROM qa_infra_marker ORDER BY value')==before
res.update(fresh_volume_marker_absent=True,old_and_dev_markers_unchanged=True,old_volume=volume,successor=successor,fresh=fresh,deletions='none')
(r/'persistence.json').write_text(json.dumps(res,indent=2));print(json.dumps(res,indent=2))
