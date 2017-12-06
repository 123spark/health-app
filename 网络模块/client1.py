# -*- coding: utf-8 -*-
import socket
import time
HOST = '127.0.0.1'
PORT = 9999

#接收所有的数据文件，并且接收一次，写入一次，从而避免内存消耗过大，在进行接收的时候，显示进度条
def recv_all(socketobj,file_name,file_size):
	f = open(r'e:\home\client\recv\%s'%file_name,'w')
	while file_size > 0:
		if file_size <= 1024:
			processbar.progressbar(10,10)
			data = socketobj.recv(1024)
			f.write(data)
			break
		elif file_size > 1024:
			processbar.progressbar(1024*10/file_size,10)
			data = socketobj.recv(1024)
			f.write(data)
			file_size -= 1024
	f.close()
s = socket.socket()
s.connect((HOST,PORT))
while True:
	commands = raw_input('>>>')
	if commands == 'exit' or not commands:break
	s.sendall(commands)
	options = commands.strip().split(' ')
	if len(options) == 2:
		file_name = options[1]
		if options[0] == 'put':
			f = open(r'e:\home\client\send\%s'%file_name,'rb')
			data = f.read()
			time.sleep(0.2)
			s.send(str(len(data)))
			time.sleep(0.2)
			s.send(data)
			print s.recv(1024)
		elif options[0] == 'get':
			file_size = int(s.recv(1024))
			recv_all(s,file_name,file_size)
			print s.recv(1024)
	else:
		pass