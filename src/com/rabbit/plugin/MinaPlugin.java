package com.rabbit.plugin;
 
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;
import com.rabbit.im.chat.handler.BindHandler;
import com.rabbit.im.chat.handler.LogoutHandler;
import com.rabbit.im.chat.handler.PushOfflineMessageHandler;
import com.rabbit.im.chat.handler.SessionClosedHandler;
import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.filter.ServerMessageCodecFactory;
import com.rabbit.im.nio.handler.HeartbeatHandler;
import com.rabbit.im.nio.handler.IMRequestHandler;
import com.rabbit.im.nio.handler.MainIOHandler;
import com.rabbit.im.nio.ssl.BogusSslContextFactory;
 
public class MinaPlugin implements IPlugin {

    private final Log log = Log.getLog(getClass());
    /** Choose your favorite port number. */
    private static final int PORT = 23456;
    /** Set this to true if you want to make the server SSL */
    private static final boolean USE_SSL = false;
    
    private static NioSocketAcceptor acceptor;
 
    public MinaPlugin() {
    }
    
    public static NioSocketAcceptor getAcceptor(){
    	return acceptor;
    }
    
    public boolean start() {
    	//acceptor
    	acceptor = new NioSocketAcceptor();
    	acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 180);
		acceptor.getSessionConfig().setKeepAlive(true);
		acceptor.getSessionConfig().setTcpNoDelay(true);
    	//handler
    	MainIOHandler mainIOHandler = new MainIOHandler();
    	HashMap<String, IMRequestHandler> handlers = new HashMap<String, IMRequestHandler>();
    	handlers.put(IMConstant.RequestKey.CLIENT_BIND, new BindHandler());
    	handlers.put(IMConstant.RequestKey.CLIENT_LOGOUT, new LogoutHandler());
    	handlers.put(IMConstant.RequestKey.CLIENT_HEARTBEAT, new HeartbeatHandler());
    	handlers.put("sessionClosedHander", new SessionClosedHandler());
    	handlers.put(IMConstant.RequestKey.CLIENT_OFFLINE_MESSAGE, new PushOfflineMessageHandler());
    	mainIOHandler.setHandlers(handlers);
    	//filter chain
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();        
		acceptor.getFilterChain().addLast("executor", new ExecutorFilter(5,10));
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ServerMessageCodecFactory()));
        if (USE_SSL) {
            try {
            	 SslFilter sslFilter = new SslFilter(BogusSslContextFactory.getInstance(true));
                 chain.addLast("sslFilter", sslFilter);
			} catch (Exception e) {
				log.error("SSL support error:"+e.getMessage());
				return false;
			}
        }
        //bind
        acceptor.setHandler(mainIOHandler);
        try {
			acceptor.bind(new InetSocketAddress(PORT));
		} catch (IOException e) {
			log.error("Mina server start error:"+e.getMessage());
			return false;
		}
        log.info("Mima server start on port :"+PORT);
        return true;
    }
 
 
    public boolean stop() {
    	acceptor.dispose();
        return true;
    }
 
}