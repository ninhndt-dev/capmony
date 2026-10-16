import os,subprocess,json,urllib.request,pathlib
os.environ.update(DOCKER_CONFIG=os.getcwd()+'/.local/docker-cli',DOCKER_HOST='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock')
r=pathlib.Path('.local/qa-mvc-final')
def d(*a):return subprocess.check_output(['docker',*a],text=True).strip()
old='capmoney-mvc-qa-probe-caddy-1';name='capmoney-mvc-qa-caddy-successor';net='capmoney-mvc-qa-probe_api_net'
a=json.loads(d('inspect',old))[0];oldip=a['NetworkSettings']['Networks'][net]['IPAddress'];image=a['Image']
d('run','-d','--name',name,'--network',net,'--mount','type=bind,src='+os.getcwd()+'/infra/Caddyfile,dst=/etc/caddy/Caddyfile,readonly','--mount','type=bind,src='+str((r/'secrets/edge-auth').resolve())+',dst=/run/secrets/edge_auth,readonly',image)
d('network','connect','capmoney-mvc-qa-probe_web_net',name)
newip=json.loads(d('inspect',name))[0]['NetworkSettings']['Networks'][net]['IPAddress'];assert oldip!=newip
# Connect internal curl utility to API subnet, reach successor directly on8080; correct Origin required.
status=d('run','--name','capmoney-mvc-qa-caddy-successor-check','--network',net,'curlimages/curl:8.16.0','-sS','--retry','10','--retry-connrefused','--retry-delay','1','-o','/dev/null','-w','%{http_code}','-X','POST','-H','Origin: http://127.0.0.1:28082','http://'+newip+':8080/api/__probe');assert status=='200'
res=dict(old_container=old,old_ip=oldip,new_container=name,new_ip=newip,status=int(status),same_secret=True)
(r/'caddy-successor.json').write_text(json.dumps(res,indent=2));print(res)
