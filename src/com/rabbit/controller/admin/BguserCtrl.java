package com.rabbit.controller.admin;

import com.rabbit.model.Admin;
import com.rabbit.model.Role;
import com.rabbit.util.SecurityUtil;
import com.jfinal.kit.StrKit;
import com.rabbit.controller.common.AdminRootCtrl;
import com.rabbit.jfinal.ext.authorize.Authorize;
@Authorize(name = "2", value = "用户管理")
public class BguserCtrl extends AdminRootCtrl {	

	public void index(){
		render("index.html");
	}

	public void profile(){
		renderJson(Admin.dao.paginate(getParaToInt("page",1),getParaToInt("rp",10), getPara("param")));
	}
	
	@Authorize(name = "2_3", value = "删除")
	public void delete(){
		boolean ret = false;
		if(StrKit.notBlank(getPara("id"))){
			ret = Admin.dao.deleteById(getPara("id"));
		}
		if(ret)renderJson("msg","success");
		else renderJson("msg","删除失败，请重试！");
	}

	@Authorize(name = "2_1", value = "新增")
	public void add(){
		setAttr("rolelist",Role.dao.findAll());
		render("edit.html");
	}

	@Authorize(name = "2_2", value = "修改")
	public void edit(){
		Admin admin = Admin.dao.findById(getPara(0));
		if(admin!=null){
			setAttr("admin",admin);
			setAttr("rolelist",Role.dao.findAll());
			render("edit.html");
		}
		else renderNull();
	}
	
	public void save(){
		boolean ret = false;
		if(StrKit.notBlank(getPara("admin.id"))){
			Admin admin = getModel(Admin.class);
			if(StrKit.notBlank(admin.getStr("password"))){
				admin.set("password", SecurityUtil.md5(admin.getStr("password")));
			}
			else admin.remove("password");
			ret = admin.update();
		}
		else {
			ret = getModel(Admin.class).save();
		}
		if(ret)renderJson("msg","success");
		else renderJson("msg","保存失败，请重试！");
	}
}