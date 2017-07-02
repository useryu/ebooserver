package cn.ebooboo.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.Constants;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.vo.AdminResp;

public class LoginInterceptor implements Interceptor {
	
	private static final Logger logger = Logger.getLogger(LoginInterceptor.class);

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		HttpServletRequest request = controller.getRequest();
		boolean continueInvoke=true;
		if(JfinalConfig.IS_PRODUCT_ENV) {
			HttpServletResponse response = controller.getResponse();
			LoginService service = new LoginService(request, response);
			try {
				UserInfo userInfo = service.check();
				String userid = request.getHeader(Constants.WX_HEADER_ID);
				controller.setAttr("wxUser", userInfo);
				controller.setAttr("userid", userid);
				logger.info("login intercept done:"+userInfo);
			} catch (LoginServiceException e) {
				e.printStackTrace();
				continueInvoke=false;
				logger.info("login intercept faild:"+e.getMessage());
			} catch (ConfigurationException e) {
				e.printStackTrace();
				logger.info("login intercept faild:"+e.getMessage());
			}
		} else {
			System.out.println("is none prod user");
			UserInfo testUser = new UserInfo();
			controller.setAttr("wxUser", testUser);
			controller.setAttr("userid", "test_userid");
			logger.info("login intercept done in test env:"+testUser);
		}
		if(continueInvoke) {
			inv.invoke();
		} else {
			AdminResp resp = new AdminResp(0,"请登录");
			controller.renderJson(resp);
		}
	}

}
