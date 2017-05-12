package cn.ebooboo.controller;

import com.jfinal.aop.Before;

import cn.ebooboo.common.interceptor.LoginInterceptor;

@Before(LoginInterceptor.class)
public class FrontBaseController extends BaseController {

}
