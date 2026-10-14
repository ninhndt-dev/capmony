exec(open('.local/qa-java-run-v2.py').read().split('for project')[0])
base=['docker','compose','-p','capmoney-java-qa-v2','-f','infra/compose.yaml']
before=json.loads(run('docker','inspect','capmoney-java-qa-v2-api-1'))[0]['Image']
subprocess.run(base+['stop'],check=True)
subprocess.run(base+['up','--no-recreate','--no-build','-d'],check=True)
subprocess.run(['python3','.local/qa-java-runtime-v2.py'],check=True)
after=json.loads(run('docker','inspect','capmoney-java-qa-v2-api-1'))[0]['Image'];assert before==after
subprocess.run(['python3','.local/qa-java-inspect-v2.py'],check=True)
marker=run('docker','exec','capmoney-java-qa-v2-postgres-1','psql','-U','capmoney_qa','-d','capmoney_qa','-At','-c','SELECT value FROM qa_infra_marker');assert marker=='qa_310ad7f'
subprocess.run(['docker','start','-a','capmoney-java-qa-v2-mailcheck-run-9b63e675caea'],check=True)
state=json.loads(run('docker','inspect','capmoney-java-qa-v2-mailcheck-run-9b63e675caea'))[0]['State'];assert state['ExitCode']==0
(root/'regression.json').write_text(json.dumps(dict(runtime_image_unchanged=before,marker=marker,mail_runner_exit=state['ExitCode'],route_outage_topology_repeated=True),indent=2))
