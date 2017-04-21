package cn.ebooboo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.common.interceptor.AdminInterceptor;

@Before(AdminInterceptor.class)
public class BaseAdminController extends Controller{
	
	public JSONObject getReqJson(HttpServletRequest request, HttpServletResponse response) {
		
		String requestContent = getRequestJsonStr(request);
		
		// 2. 读取报文内容成 JSON 并保存在 body 变量中
		JSONObject body = new JSONObject(requestContent);
		return body;
	}

	public String getRequestJsonStr(HttpServletRequest request) {
		String requestContent = null;

		// 1. 读取报文内容
		try {
			BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
			requestContent = "";
			for (String line; (line = requestReader.readLine()) != null;) {
				requestContent += line;
			}
		} catch (IOException e) {
			
		}
		return requestContent;
	}

	public void setAllowCrossDomain(HttpServletResponse response) {
		if (!JfinalConfig.IS_PRODUCT_ENV) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
		}
	}

}
