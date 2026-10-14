exec(open('.local/mvc-qa-harness/qa-java-run-v2.py').read().split("for project")[0])
subprocess.run(['docker','compose','-p','capmoney-mvc-qa','-f','infra/compose.yaml','-f',str(root/'runtime.yaml'),'--profile','tools','run','--no-deps','mailcheck'],check=True)
