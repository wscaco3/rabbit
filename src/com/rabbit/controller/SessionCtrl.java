package com.rabbit.controller;


import java.util.Collection;

import com.rabbit.im.nio.mutual.Message;
import com.rabbit.im.nio.session.CIMSession;
import com.rabbit.im.nio.session.DefaultSessionManager;
import com.rabbit.im.client.push.SystemMessagePusher;
import com.jfinal.core.Controller;
public class SessionCtrl extends Controller {
	   
	 public void list()	 {  
		 Collection<CIMSession> csc = new DefaultSessionManager().getSessions();
		 renderText("ok");
	}
 
	 public void offline() {  
		 
		 String account = getPara("account");
		  Message msg = new Message();
		  msg.setType("999");//强行下线消息类型
		  msg.setReceiver(account);		  
		 //向客户端 发送消息
	     new SystemMessagePusher().pushMessageToUser(msg);
	 
	}
}
