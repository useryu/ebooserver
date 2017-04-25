package cn.ebooboo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ResourceController extends BaseController{

	public void index() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		String filename = super.getPara("filename");
		super.renderFile(filename);
	}
	
}
