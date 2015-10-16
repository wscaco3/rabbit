 
package com.rabbit.im.client.handler;

import org.apache.log4j.Logger;

import com.rabbit.im.nio.constant.CIMConstant;
import com.rabbit.im.nio.handler.CIMRequestHandler;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.CIMSession;
import com.rabbit.im.nio.session.DefaultSessionManager;

/**
 * 断开连接，清除session
 * 
 * @author
 */
public class SessionClosedHandler implements CIMRequestHandler {

	protected final Logger logger = Logger.getLogger(SessionClosedHandler.class);

	public ReplyBody process(CIMSession ios, SentBody message) {

		DefaultSessionManager sessionManager  =  new DefaultSessionManager();

		if(ios.getAttribute(CIMConstant.SESSION_KEY)==null)
		{
			return null;
		}
		
	    String account = ios.getAttribute(CIMConstant.SESSION_KEY).toString();
	    sessionManager.removeSession(account);
	    
		return null;
	}
	
 
	
}