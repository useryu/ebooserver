package cn.ebooboo.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.kit.PropKit;

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
			s.loadAll(PropKit.get("file_download_path")+File.separator+"zip"+File.separator+level+".zip");
		} catch (IOException e) {
			e.printStackTrace();
			super.renderJson(e.getMessage());
		}
		super.renderText("ok");
	}
	
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
