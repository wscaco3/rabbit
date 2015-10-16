package com.rabbit.plugin.mina;
 
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.rabbit.im.client.handler.BindHandler;
import com.rabbit.im.client.handler.LogoutHandler;
import com.rabbit.im.client.handler.PushOfflineMessageHandler;
import com.rabbit.im.client.handler.SessionClosedHandler;
import com.rabbit.im.nio.handler.CIMRequestHandler;
import com.rabbit.im.nio.handler.HeartbeatHandler;
import com.jfinal.plugin.IPlugin;
import com.rabbit.im.nio.handler.MainIOHandler;
 
public class MinaPlugin implements IPlugin {
 
    private final Logger logger = Logger.getLogger(getClass());
    private NioSocketAcceptor acceptor;
 
    public MinaPlugin() {
    	acceptor = new NioSocketAcceptor();
    	acceptor.setReuseAddress(true);
    	MainIOHandler mainIOHandler = new MainIOHandler();
    	HashMap<String, CIMRequestHandler> handlers = new HashMap<String, CIMRequestHandler>();
    	handlers.put("client_bind", new BindHandler());
    	handlers.put("client_logout", new LogoutHandler());
    	handlers.put("client_heartbeat", new HeartbeatHandler());
    	handlers.put("sessionClosedHander", new SessionClosedHandler());
    	handlers.put("client_get_offline_message", new PushOfflineMessageHandler());
    	mainIOHandler.setHandlers(handlers);
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
    }
 
 
    public boolean start() {
        return true;
    }
 
    public boolean stop() {
 
 
        return true;
    }
 
  
 
}