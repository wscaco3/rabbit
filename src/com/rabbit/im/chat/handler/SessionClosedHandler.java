 
package com.rabbit.im.chat.handler;

import com.jfinal.log.Log;
import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.handler.IMRequestHandler;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;
import com.rabbit.im.nio.session.SessionManager;
import com.rabbit.im.nio.session.SessionManagerFactory;

/**
 * 断开连接，清除session
 * 
 * @author
 */
public class SessionClosedHandler implements IMRequestHandler {

    private final Log log = Log.getLog(getClass());

	public ReplyBody process(IMSession ios, SentBody message) {

		SessionManager sessionManager = SessionManagerFactory.getCurrentSessionManager();

		if(ios.getAttribute(IMConstant.SESSION_KEY)==null)		{
			return null;
		}		
	    String account = ios.getAttribute(IMConstant.SESSION_KEY).toString();
	    sessionManager.removeSession(account);	    
	    
		return null;
	}
	
 
	
}