package com.rabbit.jfinal.ext.authorize;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

public class AuthModel {	

	private Map<String,Class<? extends Controller>> clzmap;
	private Map<String,Method> mtdmap;
	private Map<String,String> namemap;
	
	public AuthModel(Routes routes) {		
		super();
		clzmap = new HashMap<String,Class<? extends Controller>>();
		mtdmap = new HashMap<String,Method>();
		namemap = new HashMap<String,String>();
		for (Entry<String, Class<? extends Controller>> entry : routes.getEntrySet()) {
			Class<? extends Controller> controllerClass = entry.getValue();
			Authorize cauth = controllerClass.getAnnotation(Authorize.class);
			if(cauth!=null){
				clzmap.put(cauth.name(), controllerClass);
				namemap.put(cauth.name(), cauth.value());
			}
			Method[] methods = controllerClass.getMethods();
			for (Method method : methods) {
				Authorize mauth = method.getAnnotation(Authorize.class);
				if(mauth!=null){
					mtdmap.put(mauth.name(), method);
					namemap.put(mauth.name(), mauth.value());
				}			
			}
		}
	}
	
	public Map<String,String> getAmNames(){
		return namemap;		
	}


	public boolean access(String name,Controller controller,Method mtd){
		if(mtdmap.containsValue(mtd)){			
			if(mtdmap.get(name)!=null&&mtdmap.get(name).equals(mtd)){
				return true;
			}
			else return false;
		}
		else{
			if(clzmap.containsValue(controller.getClass())){			
				if(clzmap.get(name)!=null&&clzmap.get(name).equals(controller.getClass())){
					return true;
				}
				else return false;
			}
			else return true;
		}
		
	}
	
}
