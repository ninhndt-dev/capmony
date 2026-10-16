exec(open('.local/qa-java-inspect-v2.py').read().split('cs=')[0])
cs=json.loads(d('ps','-a','--format','{{json .}}').strip().replace('}\n{','},{').join(['[',']'])) if False else [json.loads(x) for x in d('ps','-a','--format','{{json .}}').splitlines() if 'capmoney-java-qa-v2' in x]
rows=[]
for c in cs:
 a=json.loads(d('inspect',c['ID']))[0];rows.append(dict(name=a['Name'],id=a['Id'],image=a['Image'],state=a['State']['Status'],exit_code=a['State']['ExitCode'],mounts=[{k:m[k] for k in ['Type','Source','Destination'] if k in m} for m in a['Mounts']]))
res=dict(commit='310ad7fbf2e86d7d3b682bbfdb303af02314b34e',containers=rows,java=d('exec','capmoney-java-qa-v2-api-1','java','--version'),compose=d('compose','version'))
pathlib.Path('.scratch/capmoney-infrastructure/evidence/java-final/identity.json').write_text(json.dumps(res,indent=2))
