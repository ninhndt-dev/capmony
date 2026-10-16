exec(open('.local/qa-java-inspect-v2.py').read().split('cs=')[0])
import zipfile,hashlib
subprocess.run(['docker','cp','capmoney-java-qa-v2-probe-api-1:/app/app.jar',str(r/'probe.jar')],check=True)
with zipfile.ZipFile(r/'runtime.jar') as a,zipfile.ZipFile(r/'probe.jar') as b:
 names=[n for n in a.namelist() if n.startswith('BOOT-INF/classes/') and n.endswith('.class')]
 assert names and all(a.read(n)==b.read(n) for n in names)
 assert 'BOOT-INF/classes/com/capmoney/api/ProbeController.class' in b.namelist()
 result={'same_main_class_bytes':True,'main_classes_compared':len(names)}
values=[p.read_bytes() for p in (r/'secrets').iterdir()]
tracked=subprocess.check_output(['git','ls-files','-z']).split(b'\0')
assert all(not any(v in pathlib.Path(p.decode()).read_bytes() for v in values) for p in tracked if p and pathlib.Path(p.decode()).is_file())
for name in ['api','web']:
 history=d('history','--no-trunc','capmoney-java-qa-v2-'+name)
 assert all(v.decode() not in history for v in values)
 with zipfile.ZipFile(r/'runtime.jar') as z:assert all(not any(v in z.read(n) for v in values) for n in z.namelist() if not n.endswith('/'))
result.update(secret_values_absent_git_jar_image_history=True,commit=subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip())
(r/'artifacts.json').write_text(json.dumps(result,indent=2));print(result)
