package com.rabbit.plugin;
 
import java.io.IOException;

import org.eclipse.moquette.server.Server;
import org.eclipse.moquette.server.config.ClasspathConfig;
import org.eclipse.moquette.server.config.IConfig;

import com.jfinal.plugin.IPlugin;
public class MqttPlugin implements IPlugin {
 
	private static Server mqttBroker;
    public boolean start() {
    	final IConfig classPathConfig = new ClasspathConfig();
    	mqttBroker = new Server();
        try {
			mqttBroker.startServer(classPathConfig);
	    	return true;
		} catch (IOException e) {
	    	return false;
		}
    }
 
 
    public boolean stop() {
    	mqttBroker.stopServer();
        return true;
    }
 
}