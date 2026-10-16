import os,subprocess,json,pathlib
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
r=pathlib.Path('.local/qa-java-final-v2');values=[p.read_bytes() for p in (r/'secrets').iterdir()]
p=subprocess.Popen(['docker','image','save','capmoney-java-qa-v2-api','capmoney-java-qa-v2-web'],stdout=subprocess.PIPE)
prev=b'';count=0
while True:
 chunk=p.stdout.read(1024*1024)
 if not chunk:break
 count+=len(chunk);window=prev+chunk;assert not any(v in window for v in values),'Secret value found in image archive';prev=window[-128:]
assert p.wait()==0
(r/'image-layer-scan.json').write_text(json.dumps(dict(images=['capmoney-java-qa-v2-api','capmoney-java-qa-v2-web'],archive_bytes_scanned=count,secrets_found=False),indent=2));print('Image layer secret scan passed')
