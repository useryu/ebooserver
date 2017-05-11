package cn.ebooboo.controller;

import java.util.List;

import com.jfinal.aop.Before;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.common.interceptor.LoginInterceptor;
import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.model.User;
import cn.ebooboo.service.LoaderService;

public class ActivityController extends BaseController{

	public void list() {
		renderJson("开发中");
	}
	
}
