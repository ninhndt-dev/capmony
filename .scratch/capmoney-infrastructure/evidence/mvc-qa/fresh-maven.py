"""Run only after immutable reviewed SHA is supplied; preserve all previous targets."""
import pathlib,subprocess,sys,hashlib,shutil,json,datetime,os
expected=sys.argv[1]
actual=subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip()
assert actual==expected,'QA source SHA differs from reviewed commit'
repo=pathlib.Path.cwd();api=repo/'apps/api';stamp=datetime.datetime.now().strftime('%Y%m%d-%H%M%S');out=repo/'.local/build'/('mvc-qa-'+stamp);out.mkdir(parents=True,exist_ok=False)
files=[api/'pom.xml',api/'mvnw',api/'mvnw.cmd']+list((api/'.mvn').rglob('*'))+list((api/'src').rglob('*'));hashes={}
for p in files:
 if not p.is_file():continue
 relative=p.relative_to(api);dest=out/relative;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,dest)
 digest=hashlib.sha256(p.read_bytes()).hexdigest();assert hashlib.sha256(dest.read_bytes()).hexdigest()==digest;hashes[str(relative)]=digest
results=repo/'.local/qa-mvc-final';results.mkdir(exist_ok=True)
(results/'fresh-source.json').write_text(json.dumps(dict(commit=actual,build_directory=str(out),sha256=hashes),indent=2))
env=os.environ.copy();env.update(JAVA_HOME='/opt/homebrew/opt/openjdk@25',DOCKER_CONFIG=str(repo/'.local/docker-cli'),DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock',TESTCONTAINERS_HOST_OVERRIDE='127.0.0.1',TESTCONTAINERS_RYUK_DISABLED='true',TESTCONTAINERS_REUSE_ENABLE='true',CAPMONEY_CONTAINER_TESTS='true')
with (results/'maven-tests.log').open('w') as log:result=subprocess.run([str(out/'mvnw'),'-B','-f',str(out/'pom.xml'),'test'],env=env,stdout=log,stderr=subprocess.STDOUT)
assert result.returncode==0,'Maven failed; inspect retained log/build directory'
assert subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip()==expected
print('Fresh copied source hash matches; Maven succeeded; retained '+str(out))
