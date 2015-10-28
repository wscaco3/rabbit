package com.rabbit.controller.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.jfinal.core.AuthorAnnoInterceptor;
import com.jfinal.kit.StrKit;
import com.rabbit.controller.common.AdminRootCtrl;
import com.rabbit.jfinal.ext.authorize.Authorize;
import com.rabbit.model.Role;
@Authorize(name = "1", value = "权限管理")
public class RoleCtrl extends AdminRootCtrl {	
	
	public void index(){
		render("index.html");
	}

	public void profile(){
		renderJson(Role.dao.paginate(getParaToInt("page",1),getParaToInt("rp",10), getPara("param")));
	}
	

	@Authorize(name = "1_1", value = "新增")
	public void add(){
		//解析模块列表
		List<Map<String,Object>> modelist = new ArrayList<Map<String,Object>>();
		Map<String,String> mmap = AuthorAnnoInterceptor.getAuthModelNames();
		if(mmap!=null){
			for(String key:mmap.keySet()){
				String[] levels = key.split("_");
				Map<String,Object> pmap = new HashMap<String,Object>();
				pmap.put("id", key);
				pmap.put("name", mmap.get(key));
				if(levels.length>1){
					String subpid = "";
					for(int i=0;i<levels.length-1;i++){
						subpid +=levels[i]+"_";
					}
					pmap.put("pId", subpid.substring(0, subpid.length()-1));
				}
				else{
					pmap.put("pId", 0);
				}
				modelist.add(pmap);
			}
			Gson gson = new Gson(); 
			Collections.sort(modelist, new Comparator<Map<String,Object>>() {
		      public int compare(Map<String,Object> o1, Map<String,Object> o2) {
		        return ((String)o1.get("id")).compareTo((String)o2.get("id"));
		      }
		    });
			setAttr("modeljson",gson.toJson(modelist));
		}		
		render("edit.html");
	}
	

	@Authorize(name = "1_2", value = "修改")
	public void edit(){
		Role role = Role.dao.findById(getPara(0));
		if(role!=null){
			setAttr("role",role);
			List<Map<String,Object>> modelist = new ArrayList<Map<String,Object>>();
			Map<String,String> mmap = AuthorAnnoInterceptor.getAuthModelNames();
			if(mmap!=null){
				for(String key:mmap.keySet()){
					String[] levels = key.split("_");
					Map<String,Object> pmap = new HashMap<String,Object>();
					pmap.put("id", key);
					pmap.put("name", mmap.get(key));
					if(levels.length>1){
						String subpid = "";
						for(int i=0;i<levels.length-1;i++){
							subpid +=levels[i]+"_";
						}
						pmap.put("pId", subpid.substring(0, subpid.length()-1));
					}
					else{
						pmap.put("pId", 0);
					}
					if(StrKit.notBlank(role.getStr("models"))&&role.getStr("models").indexOf(","+key+",")!=-1){
						pmap.put("checked", true);
					}
					modelist.add(pmap);
				}
				Gson gson = new Gson(); 
				Collections.sort(modelist, new Comparator<Map<String,Object>>() {
			      public int compare(Map<String,Object> o1, Map<String,Object> o2) {
			        return ((String)o1.get("id")).compareTo((String)o2.get("id"));
			      }
			    });
				setAttr("modeljson",gson.toJson(modelist));
			}		
			render("edit.html");				
		}
		else renderNull();		
	}
	
	@Authorize(name = "1_3", value = "删除")
	public void delete(){
		boolean ret = false;
		ret = Role.dao.deleteById(getPara("id"));
		if(ret)renderJson("msg","success");
		else renderJson("msg","删除失败，请重试！");
	}
	
	public void save(){
		boolean ret = false;
		if(StrKit.notBlank(getPara("role.id"))){
			ret = getModel(Role.class).update();
		}
		else {
			ret = getModel(Role.class).save();
		}
		if(ret)renderJson("msg","success");
		else renderJson("msg","保存失败，请重试！");
	}
}