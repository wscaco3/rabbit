package com.rabbit.controller.common;

import com.jfinal.aop.Before;
import com.jfinal.core.AuthorAnnoInterceptor;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;

@Before({SessionInViewInterceptor.class,AuthorAnnoInterceptor.class})
public class AdminRootCtrl extends Controller{
}
