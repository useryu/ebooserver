package cn.ebooboo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ebooboo.service.LoaderService;


public class ResourceController extends BaseController{

	public void index() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		String filename = super.getPara("filename");
		super.renderFile(filename);
	}
	
	public void load() {
		LoaderService s = new LoaderService();
		String level=super.getPara("level");
		try {
			s.loadAll("d:/var/eboofiles/zip/"+level+".zip");
		} catch (IOException e) {
			e.printStackTrace();
			super.renderJson(e.getMessage());
		}
		super.renderText("ok");
	}
}
