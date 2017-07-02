package cn.ebooboo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.Controller;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;

public class LoginController extends BaseController{

	public void index() {
		logger.info("========= login start, UserInfo: ==========");
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		LoginService service = new LoginService(request, response);
		UserInfo userInfo = new UserInfo();
		try {
			// 调用登录接口，如果登录成功可以获得登录信息
			userInfo = service.login();
			logger.info("========= LoginSuccess, UserInfo: ==========");
			logger.info(userInfo.toString());
		} catch (LoginServiceException e) {
			// 登录失败会抛出登录失败异常
			e.printStackTrace();
			logger.info("login faild................");
			logger.info(e.getMessage());
		} catch (ConfigurationException e) {
			// SDK 如果还没有配置会抛出配置异常
			e.printStackTrace();
			logger.info("login faild................");
			logger.info(e.getMessage());
		}
		super.renderNull();
	}
	
}
