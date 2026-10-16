import socket,threading,pathlib,time,sys
root=pathlib.Path(sys.argv[1]);upstream=sys.argv[2]
def client(c):
 u=socket.create_connection((upstream,5432))
 def relay(src,dst,delayed):
  try:
   while True:
    data=src.recv(65536)
    if not data:break
    mode=root/'proxy-mode'
    if delayed and mode.exists() and mode.read_text()=='slow':time.sleep(6)
    dst.sendall(data)
  except OSError:pass
  finally:
   try:dst.shutdown(socket.SHUT_WR)
   except OSError:pass
 threading.Thread(target=relay,args=(c,u,False),daemon=True).start();relay(u,c,True)
s=socket.socket();s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1);s.bind(('0.0.0.0',5432));s.listen();(root/'proxy-ready').write_text('ready')
while True:
 c,_=s.accept();threading.Thread(target=client,args=(c,),daemon=True).start()
