# -*- coding: utf-8 -*-
import socket
import threading
import os
import struct
import pymssql
def Add_data(userid,userpwd):
    conn = pymssql.connect(host='ASUS-PC', user='sa', password='1', database='Health APP')
    cur = conn.cursor()
    sql="insert into UserInf(UserID,UserPassword) values(%s,%s)"%(userid,userpwd)
    cur.execute(sql)
    cur.close()
    conn.commit()
    conn.close()
def Search_pwd(userid):
    conn = pymssql.connect(host='ASUS-PC', user='sa', password='1', database='Health APP')
    cur = conn.cursor()
    sql ="select UserPassword from UserInf where UserID=%s"%userid
    cur.execute(sql)
    pwd=cur.fetchone()
    #print pwd[0]
    return pwd[0]
    cur.close()
    conn.close()
def recv_data(newsock,filename,filesize):
    # filename = 'new_'+filename.strip('\00')  # 命名新文件new_传送的文件
    filename = filename.strip('\00')
    fp = open(r'f:\%s' % filename, 'ab+')  # 新建文件，并且准备写入
    restsize = filesize
    print "recving..."
    filesize_old = os.stat(r'f:\%s' % filename).st_size
    while 1:
        if restsize > 102400:  # 如果剩余数据包大于1024，就去1024的数据包
            filedata = newsock.recv(10240)
        else:
            filedata = newsock.recv(restsize)
            fp.write(filedata)
            # break
        if not filedata:
            break
        restsize = restsize - len(filedata)  # 计算剩余数据包大小
        if restsize <= 0:
            break
    fp.close()
    filesize_new = os.stat(r'f:\%s' % filename).st_size
    #print filesize1
    if filesize_new-filesize_old == filesize:
        print "receive sucessful"
        msg = struct.pack('ii', 4,1)
        newsock.send(msg)
    else:
        print "receive failed"
        msg = struct.pack('ii', 4,0)
        newsock.send(msg)
def function(newsock, address):
    FILEINFO_SIZE = struct.calcsize('i50sI')
    '''定义文件信息（包含文件名和文件大小）大小。128s代表128个char[]（文件名），I代表一个integer or long（文件大小）'''
    while 1:
        try:
            fhead = newsock.recv(FILEINFO_SIZE)
            opcode,filename,filesize = struct.unpack('i50sI', fhead)
            '''把接收到的数据库进行解包，按照打包规则128sI'''
            print "address is: ", address
            print "opcode is %s"%opcode
            print  "msg is %s"%filename
            print "filesize is %s"%filesize
            if opcode==1:
                print "注册信息"
                userid,userpwd=filename.strip("\00").split(' ')
                Add_data(userid,userpwd)
                pwd=Search_pwd(userid)
                pwd=pwd.encode("utf-8")
                if pwd==userpwd:
                    msg = struct.pack('ii', 4, 1)
                    newsock.send(msg)
                else:
                    msg = struct.pack('ii', 4,0)
                    newsock.send(msg)
            elif opcode==2:
                print "登录信息"
                userid, userpwd = filename.strip("\00").split(' ')
                pwd = Search_pwd(userid)
                pwd=pwd.encode("utf-8")
                if pwd == userpwd:
                    msg = struct.pack('ii', 4, 1)
                    newsock.send(msg)
                else:
                    msg = struct.pack('ii', 4,0)
                    newsock.send(msg)
            elif opcode==3:
                print "接收文件"
                recv_data(newsock, filename, filesize)
            else :
                print "invalid command"
                msg = struct.pack('ii', 4, 999)
                newsock.send(msg)
        except Exception, e:
            print unicode(e).encode('gbk')
            print "the socket partner maybe closed"
            newsock.close()
            break
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # 创建tcp连接
sock.bind(('192.168.1.106', 8887))  # 定于端口和ip
sock.listen(5)  # 监听
while True:
    newsock, address = sock.accept()
    print "accept another connection"
    tmpThread = threading.Thread(target=function, args=(newsock, address))  # 如果接收到文件，创建线程
    tmpThread.start()  # 执行线程
print 'end'