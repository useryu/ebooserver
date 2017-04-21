package cn.ebooboo.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.Constants;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;

import cn.ebooboo.JfinalConfig;

public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		HttpServletRequest request = controller.getRequest();
		if(JfinalConfig.IS_PRODUCT_ENV) {
			LoginService service = new LoginService(request, controller.getResponse());
			try {
				UserInfo userInfo = service.check();
				String userid = request.getHeader(Constants.WX_HEADER_ID);
				controller.setAttr("wxUser", userInfo);
				controller.setAttr("userid", userid);
			} catch (LoginServiceException e) {
				e.printStackTrace();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("is none prod user");
			UserInfo testUser = new UserInfo();
			controller.setAttr("wxUser", testUser);
			controller.setAttr("userid", "test_userid");
		}
		inv.invoke();
	}

}
