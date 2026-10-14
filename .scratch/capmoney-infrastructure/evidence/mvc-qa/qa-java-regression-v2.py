exec(open('.local/mvc-qa-harness/qa-java-run-v2.py').read().split('for project')[0])
base=['docker','compose','-p','capmoney-mvc-qa','-f','infra/compose.yaml','-f',str(root/'runtime.yaml')]
before=json.loads(run('docker','inspect','capmoney-mvc-qa-api-1'))[0]['Image']
subprocess.run(base+['stop'],check=True)
subprocess.run(base+['up','--no-recreate','--no-build','-d'],check=True)
subprocess.run(['python3','.local/mvc-qa-harness/qa-java-runtime-v2.py'],check=True)
after=json.loads(run('docker','inspect','capmoney-mvc-qa-api-1'))[0]['Image'];assert before==after
subprocess.run(['python3','.local/mvc-qa-harness/qa-java-inspect-v2.py'],check=True)
marker=run('docker','exec','capmoney-mvc-qa-postgres-1','psql','-U','capmoney_qa','-d','capmoney_qa','-At','-c','SELECT value FROM qa_infra_marker');assert marker=='qa_mvc_48'
mail_runner=run('docker','ps','-a','--filter','label=com.docker.compose.project=capmoney-mvc-qa','--filter','label=com.docker.compose.service=mailcheck','--format','{{.Names}}').splitlines()[-1]
subprocess.run(['docker','start','-a',mail_runner],check=True)
state=json.loads(run('docker','inspect',mail_runner))[0]['State'];assert state['ExitCode']==0
(root/'regression.json').write_text(json.dumps(dict(runtime_image_unchanged=before,marker=marker,mail_runner_exit=state['ExitCode'],route_outage_topology_repeated=True),indent=2))
