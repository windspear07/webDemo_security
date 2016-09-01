# -*- coding: utf-8 -*-
from Tkinter import *
import webbrowser
import hashlib
import uuid
import urllib2
import json

'''
参考文档《平台安全客户端设计》
1 功能说明
    （1）登录。输入user/password，获取mac后进行登录校验，直接打开默认浏览器带参数打开登录页面。
    （2）查看本机mac地址。供服务商确认信息用。
2 登录流程说明
    （1）输入u、p
    （2）获取mac
    （3）开启登录session，获取动态dynamic_salt
    （4）生成登录参数，打开浏览器
    u=user1
    p=sha256(  md5(password1) + ’uuid_salt_value’ )
    m=sha256(  md5(l_mac) + ’uuid_salt_value’ )
3 web模块mock功能
    （1）get_salt接口。返回uuid形式的salt，map.push('user','salt');
    （2）login接口。接受u、p、m，根据key=user进行判断
    校验方式如上加密方式。

2016-08-09
1 完成web mock搭建。
2 完成客户端程序基本功能
3 居中显示。

客户端todos
1 提供显示mac地址的弹窗功能。
2 提供版本更新功能
3 本地配置文件
4 编译多个系统版本并测试
'''
#title
main_title='安全登录客户端v1.2'
#服务器接口地址
url_login = "http://localhost:8080/login"
get_salt_url = "http://localhost:8081/salt?u=" #"http://localhost/get_salt"
#登录参数名称
para_user="u" #用户
para_password="p" #密码
para_mac="m" #mac地址

def pr(s):
    print(s)

def login_action():
    user = user_entry.get()
    password = pwd_entry.get()
    pr('=>>' + user + ',' + password)

    #gen salt from server
    response = urllib2.urlopen(get_salt_url + user)
    salt = str(response.read())
    response.close()
    pr('get salt= >' + salt)

    #gen encrypt password
    pwd_md5 = hashlib.md5(password).hexdigest()
    pwd_sha256 = hashlib.sha256( pwd_md5 + salt).hexdigest()
    pr('encryp password md5=>' + pwd_md5)
    pr('encryp password sha256=>' + pwd_sha256)

    #gen encrypt mac
    node = uuid.getnode()
    mac = uuid.UUID(int = node).hex[-12:]
    #mac = "adflkasdjfal"
    mac_md5 = hashlib.md5(mac).hexdigest()
    mac_sha256 = hashlib.sha256( mac_md5 + salt ).hexdigest()
    pr('row mac=>' + mac)
    pr('encryp mac md5=>' + mac_md5)
    pr('encryp mac sha256=>' + mac_sha256)

    #gen url
    u = "?" + para_user + "=" + user
    p = "&" + para_password + "=" + pwd_sha256
    m = "&" + para_mac + "=" + mac_sha256
    login_url = url_login + u + p + m
    pr(login_url)

    #open browser
    webbrowser.open( login_url );

def login_action_enter(event):
    login_action()

root = Tk()
root.wm_title('sebe Login')

l1=Label(root,text="用户名：")
l1.grid(row=1,sticky=W)
user_entry=Entry(root)
user_entry.grid(row=1,column=1,sticky=E)

l2=Label(root,text="密码：")
l2.grid(row=2,sticky=W)
pwd_entry=Entry(root)
pwd_entry['show']='*'
pwd_entry.grid(row=2,column=1,sticky=E)
pwd_entry.bind('<Key-Return>',login_action_enter)

l_button=Button(root,text="登录",command=login_action)
l_button.grid(row=3,column=1,sticky=E)
#l_button.bind('<Button-1>',login_action)

c=Label(root,text='')
c.grid(row=4)

root.resizable(False,False)
root.title(main_title)

root.update() # update window ,must do
curWidth = root.winfo_reqwidth() # get current width
curHeight = root.winfo_height() # get current height
scnWidth,scnHeight = root.maxsize() # get screen width and height
# now generate configuration information
tmpcnf = '%dx%d+%d+%d'%(curWidth,curHeight,(scnWidth-curWidth)/2,(scnHeight-curHeight)/2)
root.geometry(tmpcnf)

root.update()
root.deiconify()
root.mainloop()
