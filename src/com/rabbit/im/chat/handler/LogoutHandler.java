 
package com.rabbit.im.chat.handler;

import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.handler.IMRequestHandler;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;
import com.rabbit.im.nio.session.SessionManager;
import com.rabbit.im.nio.session.SessionManagerFactory;
 

/**
 * 退出连接实现
 * 
 *  @author easy 
 */
public class LogoutHandler implements IMRequestHandler {

	public ReplyBody process(IMSession ios, SentBody message) {
		SessionManager sessionManager = SessionManagerFactory.getCurrentSessionManager();
		
		String account =ios.getAttribute(IMConstant.SESSION_KEY).toString();
		ios.removeAttribute(IMConstant.SESSION_KEY);
		ios.close(true);
	
		sessionManager.removeSession(account);
		 
		return null;
	}
	
	
}