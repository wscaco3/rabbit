package com.rabbit.controller.admin;

import com.jfinal.core.AuthorAnnoInterceptor;
import com.rabbit.controller.common.AdminRootCtrl;

public class AdminCtrl extends AdminRootCtrl{
	
	public void center(){
		System.out.println(AuthorAnnoInterceptor.getAuthModelNames().toString());
		render("center.html");
	}
	
}
