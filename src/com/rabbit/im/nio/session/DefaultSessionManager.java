package com.rabbit.im.nio.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import com.rabbit.im.nio.constant.IMConstant;

/**
 * 自带默认 session管理实现， 各位可以自行实现 AbstractSessionManager接口来实现自己的 session管理 服务器集群时
 * 须要将CIMSession 信息存入数据库或者nosql 等 第三方存储空间中，便于所有服务器都可以访问
 * 
 * @author easy
 */
public class DefaultSessionManager implements SessionManager {

	private static HashMap<String, IMSession> sessions = new HashMap<String, IMSession>();

	private static final AtomicInteger connectionsCounter = new AtomicInteger(0);

	private static DefaultSessionManager defaultInstance = null;

	private DefaultSessionManager() {
	}

	public static DefaultSessionManager getInstance() {
		DefaultSessionManager retInstance = null;
		synchronized (DefaultSessionManager.class) {
			if (defaultInstance == null) {
				defaultInstance = new DefaultSessionManager();
			}
		}
		retInstance = defaultInstance;
		return retInstance;
	}

	/**
     *  
     */
	public void addSession(String account, IMSession session) {
		if (session != null) {
			session.setAttribute(IMConstant.SESSION_KEY, account);
			sessions.put(account, session);
			connectionsCounter.incrementAndGet();
		}

	}

	public IMSession getSession(String account) {

		return sessions.get(account);
	}

	public Collection<IMSession> getSessions() {
		return sessions.values();
	}

	public void removeSession(IMSession session) {

		sessions.remove(session.getAttribute(IMConstant.SESSION_KEY));
	}

	public void removeSession(String account) {

		sessions.remove(account);

	}

	public boolean containsIMSession(IMSession ios) {
		return sessions.containsKey(ios.getAttribute(IMConstant.SESSION_KEY))
				|| sessions.containsValue(ios);
	}

	public String getAccount(IMSession ios) {
		if (ios.getAttribute(IMConstant.SESSION_KEY) == null) {
			for (String key : sessions.keySet()) {
				if (sessions.get(key).equals(ios)
						|| sessions.get(key).getNid() == ios.getNid()) {
					return key;
				}
			}
		} else {
			return ios.getAttribute(IMConstant.SESSION_KEY).toString();
		}

		return null;
	}

}
