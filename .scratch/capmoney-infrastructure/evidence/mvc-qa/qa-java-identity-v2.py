exec(open('.local/mvc-qa-harness/qa-java-inspect-v2.py').read().split('cs=')[0])
cs=json.loads(d('ps','-a','--format','{{json .}}').strip().replace('}\n{','},{').join(['[',']'])) if False else [json.loads(x) for x in d('ps','-a','--format','{{json .}}').splitlines() if 'capmoney-mvc-qa' in x]
rows=[]
for c in cs:
 a=json.loads(d('inspect',c['ID']))[0];rows.append(dict(name=a['Name'],id=a['Id'],image=a['Image'],state=a['State']['Status'],exit_code=a['State']['ExitCode'],mounts=[{k:m[k] for k in ['Type','Source','Destination'] if k in m} for m in a['Mounts']]))
res=dict(commit=subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip(),containers=rows,java=d('exec','capmoney-mvc-qa-api-1','java','--version'),compose=d('compose','version'))
pathlib.Path('.scratch/capmoney-infrastructure/evidence/mvc-qa/identity.json').write_text(json.dumps(res,indent=2))
