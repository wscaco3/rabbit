package com.rabbit.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.rabbit.model.Admin;
import com.rabbit.model.Role;
import com.rabbit.util.SecurityUtil;

public class IndexCtrl extends Controller {

	public void index(){
		render("index.html");
	}	
	
	public void adminlogin(){
		if(StrKit.isBlank(getPara("username"))||StrKit.isBlank(getPara("password"))){
			renderJson("msg","用户和密码不能为空！");
		}
		else{
			String username = SecurityUtil.sqlFilter(getPara("username"));
			String epass = SecurityUtil.md5(getPara("password"));
			Admin bguser = Admin.dao.findByUserPass(username,epass);
			if(bguser!=null&&bguser.getInt("stat").intValue()==1){
				setSessionAttr("bguser",bguser);
				Role role = Role.dao.findById(bguser.getInt("rid"));
				if(role!=null){
					setSessionAttr("usermodels",role.getStr("models"));
				}
				renderJson("msg","success");
			}
			else{
				renderJson("msg","用户名或密码不正确！");
			}
		}
	}
}





