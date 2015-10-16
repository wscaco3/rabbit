package com.rabbit.im.client.handler;

import java.net.InetAddress;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.rabbit.im.nio.constant.CIMConstant;
import com.rabbit.im.nio.handler.CIMRequestHandler;
import com.rabbit.im.nio.mutual.Message;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.CIMSession;
import com.rabbit.im.nio.session.DefaultSessionManager;
 

/**
 * 账号绑定实现
 * 
 * @author
 */ 
public class BindHandler implements CIMRequestHandler {

	protected final Logger logger = Logger.getLogger(BindHandler.class);
	public ReplyBody process(CIMSession newSession, SentBody message) {
		
		ReplyBody reply = new ReplyBody();
		DefaultSessionManager sessionManager= new DefaultSessionManager();
		try { 
			
			String account = message.get("account");
			
			newSession.setAccount(account);
			newSession.setDeviceId(message.get("deviceId"));
			newSession.setGid(UUID.randomUUID().toString());
			newSession.setHost(InetAddress.getLocalHost().getHostAddress());
			newSession.setChannel( message.get("channel"));
			newSession.setDeviceModel(message.get("device"));
            //第一次设置心跳时间为登录时间
			newSession.setBindTime(System.currentTimeMillis());
			newSession.setHeartbeat(System.currentTimeMillis());
			/**
			 * 由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接
			 */
			CIMSession oldSession  = sessionManager.getSession(account);
            //如果是账号已经在另一台终端登录。则让另一个终端下线
			if(oldSession!=null&&!oldSession.equals(newSession))
			{
					 
					oldSession.removeAttribute(CIMConstant.SESSION_KEY);
					Message msg = new Message();
					msg.setType(CIMConstant.MessageType.TYPE_999);//强行下线消息类型
					msg.setReceiver(account);
					
					if(!oldSession.isLocalhost())
					{
						
						/*
						判断当前session是否连接于本台服务器，如不是发往目标服务器处理
						MessageDispatcher.execute(msg, oldSession.getHost());
						*/
					}else
					{
						oldSession.write(msg);
						oldSession.close(true);
						oldSession = null;
					}
					oldSession = null;
				
			}
			if(oldSession==null)
			{
				sessionManager.addSession(account, newSession);
				 
			}
			
			reply.setCode(CIMConstant.ReturnCode.CODE_200);

		} catch (Exception e) {
			reply.setCode(CIMConstant.ReturnCode.CODE_500);
			e.printStackTrace();
		}
		logger.debug("bind :account:" +message.get("account")+"-----------------------------" +reply.getCode());
		return reply;
	}
	
}