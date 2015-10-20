package com.rabbit.common;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.upload.OreillyCos;
import com.jfinal.upload.RenamePolicyCos;
import com.rabbit.controller.IndexCtrl;
import com.rabbit.controller.common.UploadFileCtrl;
import com.rabbit.model.*;
import com.rabbit.plugin.MinaPlugin;
import com.rabbit.plugin.QuartzPlugin;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setMaxPostSize(100*1024*1024);
		me.setError404View("/common/_404.html");
		me.setError500View("/common/_500.html");
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexCtrl.class);	//登录		
		me.add("/admin/uploadfile", UploadFileCtrl.class);//上传文件处理类
		me.add(new AdminRoutes()); 
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(c3p0Plugin);
		//任务插件
		QuartzPlugin quartzPlugin = new QuartzPlugin();
		me.add(quartzPlugin);
		//encache缓存插件
		me.add(new EhCachePlugin());
		//redis服务插件 
		RedisPlugin localRedis = new RedisPlugin("bbs", "localhost",6379); 
		me.add(localRedis);
		//mina插件
		me.add(new MinaPlugin());
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.setShowSql(true);//显示sql语句
		me.add(arp);
		// 映射表到模型
		arp.addMapping("bg_admin", Admin.class);
		arp.addMapping("bg_role", Role.class);	
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
	
	public void afterJFinalStart(){
		super.afterJFinalStart();
	    //设置文件上传重命名策略
	    OreillyCos.setFileRenamePolicy(new RenamePolicyCos());
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}
}
