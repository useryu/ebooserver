package cn.ebooboo.controller.admin;

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
import cn.ebooboo.controller.BaseController;

@Before(AdminInterceptor.class)
public class BaseAdminController extends BaseController{

	public void setAllowCrossDomain(HttpServletResponse response) {
		if (!JfinalConfig.IS_PRODUCT_ENV) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
		}
	}

}
