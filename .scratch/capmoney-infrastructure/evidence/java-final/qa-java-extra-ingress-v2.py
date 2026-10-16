exec(open('.local/qa-java-ingress-v2.py').read().split('cases=')[0])
import urllib.error
results=[]
for name,headers in [('origin-missing',{}),('origin-wrong',{'Origin':'http://wrong.invalid'}),('forged-caddy',{'Origin':'http://127.0.0.1:58081','X-Forwarded-For':'fake','X-Real-IP':'garbage','X-CM-Edge-Auth':'fake','Forwarded':'for=attacker','X-Forwarded-Host':'evil'})]:
 before=marker();request=urllib.request.Request('http://127.0.0.1:58081/api/__probe',data=b'',headers=headers,method='POST')
 try:
  with urllib.request.urlopen(request) as r: status=r.status;body=r.read().decode()
 except urllib.error.HTTPError as e:status=e.code;body=e.read().decode()
 after=marker();assert status==(200 if name=='forged-caddy' else 403);assert after==before+(status==200)
 results.append(dict(case=name,status=status,before=before,after=after,body=body))
# read and mutate only one character inside retained test container; do not print either value
before=marker()
cmd='key=$(cat /edge-auth); first=$(printf %s "$key" | cut -c1); rest=$(printf %s "$key" | cut -c2-); if [ "$first" = a ]; then key=b$rest; else key=a$rest; fi; curl -sS -o /dev/null -w "%{http_code}" -X POST -H "Origin: http://127.0.0.1:58081" -H "X-CM-Edge-Auth: $key" -H "X-Forwarded-For: 203.0.113.42" -H "X-Real-IP: 203.0.113.42" http://api:3000/api/__probe'
status=run('run','--name','capmoney-java-qa-v2-one-byte','--user','0','--network','capmoney-java-qa-v2-probe_api_net','--mount','type=bind,src='+secret+',dst=/edge-auth,readonly','--entrypoint','sh','curlimages/curl:8.16.0','-c',cmd)
after=marker();assert status=='403' and before==after
results.append(dict(case='one-byte-secret',status=403,before=before,after=after))
open('.local/qa-java-final-v2/extra-ingress.json','w').write(json.dumps(results,indent=2));print(json.dumps(results,indent=2))
