import os,subprocess,json,urllib.request
os.environ['DOCKER_CONFIG']=os.getcwd()+'/.local/docker-cli';os.environ['DOCKER_HOST']='unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock'
def run(*args):return subprocess.check_output(['docker',*args],text=True).strip()
def marker():
    return json.load(urllib.request.urlopen('http://127.0.0.1:28082/api/__probe'))['marker']
# existing local pinned test utility
secret=os.getcwd()+'/.local/qa-mvc-final/secrets/edge-auth'
common='-H "Origin: http://127.0.0.1:28082"'
key='-H "X-CM-Edge-Auth: $(cat /edge-auth)"'
ip='-H "X-Forwarded-For: 203.0.113.42" -H "X-Real-IP: 203.0.113.42"'
cases=[('missing-xreal',common+' '+key+' -H "X-Forwarded-For: 203.0.113.42"',403,'api_net'),('missing-xff',common+' '+key+' -H "X-Real-IP: 203.0.113.42"',403,'api_net'),('duplicate-xreal',common+' '+key+' '+ip+' -H "X-Real-IP: 203.0.113.42"',403,'api_net'),('comma-xff',common+' '+key+' -H "X-Forwarded-For: 203.0.113.42, 203.0.113.43" -H "X-Real-IP: 203.0.113.42"',403,'api_net'),('comma-xreal',common+' '+key+' -H "X-Forwarded-For: 203.0.113.42" -H "X-Real-IP: 203.0.113.42, 203.0.113.43"',403,'api_net')]
results=[]
for name,headers,expected,network in cases:
    before=marker()
    status=run('run','--name','capmoney-mvc-qa-ingress-'+name,'--user','0','--network','capmoney-mvc-qa-probe_'+network,'--mount','type=bind,src='+secret+',dst=/edge-auth,readonly','--entrypoint','sh','curlimages/curl:8.16.0','-c','curl -sS -o /dev/null -w "%{http_code}" -X POST '+headers+' http://api:3000/api/__probe')
    after=marker()
    assert int(status)==expected,(name,status)
    assert after==before+(expected==200),(name,before,after)
    results.append({'case':name,'status':int(status),'marker_before':before,'marker_after':after})
with open('.local/qa-mvc-final/ingress-extra-ip.json','w') as f: json.dump(results,f,indent=2)
print(json.dumps(results,indent=2))
