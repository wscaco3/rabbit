 
package com.rabbit.im.chat.handler;

import org.apache.log4j.Logger;

import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.handler.IMRequestHandler;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;
import com.rabbit.im.nio.session.DefaultSessionManager;

/**
 * 断开连接，清除session
 * 
 * @author
 */
public class SessionClosedHandler implements IMRequestHandler {

	protected final Logger logger = Logger.getLogger(SessionClosedHandler.class);

	public ReplyBody process(IMSession ios, SentBody message) {

		DefaultSessionManager sessionManager  = DefaultSessionManager.getInstance();

		if(ios.getAttribute(IMConstant.SESSION_KEY)==null)		{
			return null;
		}		
	    String account = ios.getAttribute(IMConstant.SESSION_KEY).toString();
	    sessionManager.removeSession(account);
	    
		return null;
	}
	
 
	
}