package com.rabbit.common;

import com.jfinal.config.Routes;
import com.rabbit.controller.admin.*;
public class AdminRoutes extends Routes {

	@Override
	public void config() {
		add("/admin", AdminCtrl.class);
		add("/admin/role", RoleCtrl.class);
		add("/admin/bguser", BguserCtrl.class);
		add("/admin/webchat", WebChatCtrl.class);
		add("/admin/immonitor", ImMonitorCtrl.class);
		/*add("/snaker/process", ProcessController.class);
		add("/snaker/surrogate", SurrogateController.class);
		add("/snaker/task", TaskController.class);
		add("/snaker/flow", FlowController.class);*/
	}

}
