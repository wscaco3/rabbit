 
package com.rabbit.im.nio.handler;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;

/**
 *  
 * 客户端请求的入口，所有请求都首先经过它分发处理
 * @author easy
 */
public class MainIOHandler extends IoHandlerAdapter {

	protected final Logger logger = Logger.getLogger(MainIOHandler.class);

	private HashMap<String, IMRequestHandler> handlers = new HashMap<String, IMRequestHandler>();


	 
	public void sessionCreated(IoSession session) throws Exception {
		logger.debug("sessionCreated()... from "+session.getRemoteAddress().toString());
	}

	 
	public void sessionOpened(IoSession session) throws Exception {

	}

	 
	public void messageReceived(IoSession ios, Object message)
			throws Exception {
		logger.debug("message: " + message.toString());

		/**
		 * flex 客户端安全策略请求，需要返回特定报文
		 */
		if(IMConstant.FLEX_POLICY_REQUEST.equals(message)){
			ios.write(IMConstant.FLEX_POLICY_RESPONSE);
			return ;
		}
		
		IMSession cimSession = new IMSession(ios);
		ReplyBody reply = new ReplyBody();
		SentBody body = (SentBody) message;
		String key = body.getKey();

		IMRequestHandler handler = handlers.get(key);
		if (handler == null) {
			reply.setCode(IMConstant.ReturnCode.CODE_405);
			reply.setCode("KEY ["+key+"] 服务端未定义");
		} else {
			reply = handler.process(cimSession, body);
		}
		
        if(reply!=null){
        	reply.setKey(key);
        	cimSession.write(reply);
    		logger.debug("-----------------------process done. reply: " + reply.toString());
        }
        //设置心跳时间 
        cimSession.setAttribute(IMConstant.HEARTBEAT_KEY, System.currentTimeMillis());
	}

	/**
	 */
	public void sessionClosed(IoSession ios) throws Exception {
		
		IMSession cimSession =new  IMSession(ios);
		try{
			logger.debug("sessionClosed()... from "+cimSession.getRemoteAddress());
			IMRequestHandler handler = handlers.get("sessionClosedHander");
			if(handler!=null && cimSession.containsAttribute(IMConstant.SESSION_KEY)){
				handler.process(cimSession, null);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 */
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		logger.warn("sessionIdle()... from "+session.getRemoteAddress().toString());
		if(!session.containsAttribute(IMConstant.SESSION_KEY)){
			session.close(true);
		}else{
			//如果5分钟之内客户端没有发送心态，则可能客户端断网，关闭连接
			Object heartbeat = session.getAttribute(IMConstant.HEARTBEAT_KEY);
			if(heartbeat!=null && System.currentTimeMillis()-Long.valueOf(heartbeat.toString()) >= 300000){
				session.close(false);
			}
		}
	}

	/**
	 */
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("exceptionCaught()... from "+session.getRemoteAddress());
		logger.error(cause);
		cause.printStackTrace();
	}

	/**
	 */
	public void messageSent(IoSession session, Object message) throws Exception {
		 //设置心跳时间 
        session.setAttribute(IMConstant.HEARTBEAT_KEY, System.currentTimeMillis());
	}


	public HashMap<String, IMRequestHandler> getHandlers() {
		return handlers;
	}


	public void setHandlers(HashMap<String, IMRequestHandler> handlers) {
		this.handlers = handlers;
	}
	
	

}