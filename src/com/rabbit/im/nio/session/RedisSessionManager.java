 
package com.rabbit.im.nio.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.plugin.MinaPlugin;

/** 
 * 集群 session管理实现示例， 各位可以自行实现 AbstractSessionManager接口来实现自己的 session管理
 *服务器集群时 须要将CIMSession 信息存入数据库或者nosql 等 第三方存储空间中，便于所有服务器都可以访问
 * @author easy
 */
public class RedisSessionManager implements SessionManager{

	private static RedisSessionManager defaultInstance = null;
	private static Cache redisCache = null;
	private static String redisKey = "imsessions";
	
	private RedisSessionManager() {
		redisCache = Redis.use();
	}

	public static SessionManager getInstance() {
		RedisSessionManager retInstance = null;
		synchronized (RedisSessionManager.class) {
			if (defaultInstance == null) {
				defaultInstance = new RedisSessionManager();
			}
		}
		retInstance = defaultInstance;
		return retInstance;
	}
	
    /**
     *  
     */
    public void addSession(String account,IMSession session) {
        /**
         * 下面 将session 存入数据库
         */
		if (session != null) {
			session.setAttribute(IMConstant.SESSION_KEY, account);
	    	redisCache.hset(redisKey, account, session);
		}
        
    }
     
    public IMSession getSession(String account) {
    	IMSession session = redisCache.hget(redisKey,account);
    	if(session!=null){
       	 	session.setIoSession(MinaPlugin.getAcceptor().getManagedSessions().get(session.getNid()));
    	}
    	return session;
    }
    
    @SuppressWarnings("unchecked")
	public Collection<IMSession> getSessions() {
    	List<IMSession> sessions = redisCache.hvals(redisKey);
    	return sessions;
    }
 
    public void removeSession(IMSession session) {
    	redisCache.hdel(redisKey, session.getAttribute(IMConstant.SESSION_KEY));
    	
    }
     
    public void removeSession(String account) { 
    	redisCache.hdel(redisKey, account);    
    }   
    
    public boolean containsIMSession(IMSession ios) {
    	return redisCache.hexists(redisKey, ios.getAttribute(IMConstant.SESSION_KEY).toString());
    }
  
    public String getAccount(IMSession ios){
       return ios.getAttribute(IMConstant.SESSION_KEY).toString();
    }
    
    public void releaseSessions(){
    	Map<Long, IoSession> iosMap = MinaPlugin.getAcceptor().getManagedSessions();
    	List<String> accountList = new ArrayList<String>();
    	for(Long key:iosMap.keySet()){
    		IoSession ios = iosMap.get(key); 
    		accountList.add(ios.getAttribute(IMConstant.SESSION_KEY).toString());
    	}
    	redisCache.hdel(redisKey, accountList.toArray());
    }
}
