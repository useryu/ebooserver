package cn.ebooboo.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import cn.ebooboo.common.interceptor.LoginInterceptor;
import cn.ebooboo.model.User;

public class TestController extends Controller{

	public void index() {
		renderJson("test");
	}
	
	@Before(LoginInterceptor.class)
	public void submitPoint() {
		int point = super.getParaToInt("point",-1);
		User user = User.dao.findById(getRequest().getAttribute("userid"));
		if(user==null) {
			user = new User();
			user.set("id",getRequest().getAttribute("userid"));
			user.set("point", point);
			user.save();
		} else {
			user.set("pre_point", user.getInt("point"));
			user.set("point", point);
			user.update();
		}
		renderJson(1);
	}
}
