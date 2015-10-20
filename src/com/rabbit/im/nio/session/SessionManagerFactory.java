package com.rabbit.im.nio.session;

public class SessionManagerFactory {
	public static SessionManager getCurrentSessionManager(){
		return DefaultSessionManager.getInstance();		
	}
}
