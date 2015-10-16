 
package com.rabbit.im.client.handler;

import com.rabbit.im.nio.constant.CIMConstant;
import com.rabbit.im.nio.handler.CIMRequestHandler;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.CIMSession;
import com.rabbit.im.nio.session.DefaultSessionManager;
 

/**
 * 退出连接实现
 * 
 *  @author easy 
 */
public class LogoutHandler implements CIMRequestHandler {

	public ReplyBody process(CIMSession ios, SentBody message) {

		
		DefaultSessionManager sessionManager  =  new DefaultSessionManager();
		
		String account =ios.getAttribute(CIMConstant.SESSION_KEY).toString();
		ios.removeAttribute(CIMConstant.SESSION_KEY);
		ios.close(true);
	
		sessionManager.removeSession(account);
		 
		return null;
	}
	
	
}