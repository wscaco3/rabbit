package com.jfinal.ext.plugin.mina;
 
import org.apache.log4j.Logger;

import com.jfinal.plugin.IPlugin;
 
public class MinaPlugin implements IPlugin {
 
    private final Logger logger = Logger.getLogger(getClass());
     
 
    public MinaPlugin() {
    	
    }
 
 
    public boolean start() {
        return true;
    }
 
    public boolean stop() {
 
 
        return true;
    }
 
  
 
}