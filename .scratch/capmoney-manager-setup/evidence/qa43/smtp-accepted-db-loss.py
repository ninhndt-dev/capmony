"""Retained stdlib SMTP fault sink. Run in retained Python tools container on case PG network.
Docker stop/start is controlled by host via marker files, never via Docker socket in sink.
Usage: python smtp-accepted-db-loss.py <retained-marker-directory>
Host waits smtp-before-accept, stops case-specific PG, writes db-stopped; sink sends250.
Message body stays in memory and is discarded; no token output/artifact.
"""
import socket,pathlib,sys,time,json
root=pathlib.Path(sys.argv[1]);root.mkdir(parents=True,exist_ok=True)
s=socket.socket();s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1);s.bind(('0.0.0.0',1025));s.listen(1)
(root/'smtp-listening').write_text('ready')
conn,_=s.accept();stream=conn.makefile('rwb',buffering=0)
def reply(value):stream.write((value+'\r\n').encode())
reply('220 capmoney-qa internal fault fixture')
in_data=False;body=[]
while True:
 line=stream.readline()
 if not line:break
 if in_data:
  if line==b'.\r\n':
   # Never persist mail text/token. Signal that SMTP DATA was received before acceptance.
   body.clear();(root/'smtp-before-accept').write_text('DATA received; waiting DB stop')
   deadline=time.monotonic()+120
   while not (root/'db-stopped').exists():
    if time.monotonic()>deadline:raise TimeoutError('host did not confirm PG stopped')
    time.sleep(.05)
   reply('250 accepted by isolated QA fixture');(root/'smtp-accepted').write_text('250 accepted after DB stopped');in_data=False
  else:body.append(line)
  continue
 cmd=line.decode(errors='replace').split(' ',1)[0].strip().upper()
 if cmd in ['EHLO','HELO']:reply('250 capmoney-qa')
 elif cmd in ['MAIL','RCPT','RSET','NOOP']:reply('250 OK')
 elif cmd=='DATA':reply('354 end with dot');in_data=True
 elif cmd=='QUIT':reply('221 bye');break
 else:reply('502 unsupported')
conn.close();s.close();(root/'smtp-finished').write_text('finished')
