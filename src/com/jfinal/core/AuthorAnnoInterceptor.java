package com.jfinal.core;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Config;
import com.jfinal.ext.authorize.AuthModel;
import com.jfinal.kit.StrKit;
import com.rabbit.model.Admin;

public class AuthorAnnoInterceptor implements Interceptor {

	private static AuthModel authmodel;	
	public static Map<String, String> getAuthModelNames(){
		if(authmodel==null){
			return null;		
		}
		else return authmodel.getAmNames();
	}

	public AuthorAnnoInterceptor() {
		super();
		if(authmodel==null){
			authmodel = new AuthModel(Config.getRoutes());			
		}
	}	
	
	public void intercept(Invocation ai) {
		boolean acc = false;
		String usermodels = ai.getController().getSessionAttr("usermodels");
		Admin user = ai.getController().getSessionAttr("bguser");
		if(user!=null&&StrKit.notBlank(usermodels)){
			String[] auths = usermodels.split(",");
			for(String auth : auths){
				acc = authmodel.access(auth, ai.getController(), ai.getMethod()); 
				if(acc) break;
			}
		}
		if(acc){
			ai.invoke();
		}
		else{
			HttpServletRequest request = ai.getController().getRequest();
			if (request.getHeader("accept").toLowerCase().indexOf("json") > -1 ||
					(request.getHeader("X-Requested-With")!= null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {  
				ai.getController().renderJson("{\"error\":\"401\",\"msg\":\"权限不足\"}");
	        } else {
	        	ai.getController().redirect("/common/_403.html");
	        }	
		}
	}

}