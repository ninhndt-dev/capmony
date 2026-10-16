exec(open('.local/qa-java-run-v2.py').read().split("for project")[0])
subprocess.run(['docker','compose','-p','capmoney-java-qa-v2','-f','infra/compose.yaml','--profile','tools','run','--build','--no-deps','mailcheck'],check=True)
