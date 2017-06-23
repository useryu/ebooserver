package cn.ebooboo.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;

import cn.ebooboo.common.interceptor.LoginInterceptor;
import cn.ebooboo.service.LoaderService;


public class ResourceController extends FrontBaseController{

	@Clear(LoginInterceptor.class)
	public void index() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		String filename = super.getPara("filename");
		super.getResponse().reset();
		super.renderFile(filename);
	}
	
	@Clear(LoginInterceptor.class)
	public void load() {
		LoaderService s = new LoaderService();
		String level=super.getPara("level");
		try {
			s.loadAll(PropKit.get("res_load_path")+File.separator+level+".zip");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			super.renderJson(e.getMessage());
		}
		super.renderText("ok");
	}
	
	@Clear(LoginInterceptor.class)
	public void remove() {
		LoaderService s = new LoaderService();
		int level=super.getParaToInt("level");
		try {
			s.removeAllInLevel(level);
		} catch (Exception e) {
			e.printStackTrace();
			super.renderJson(e.getMessage());
		}
		super.renderText("ok");
	}
}
