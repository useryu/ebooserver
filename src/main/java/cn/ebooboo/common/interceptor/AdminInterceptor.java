package cn.ebooboo.common.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import cn.ebooboo.JfinalConfig;

public class AdminInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		HttpServletRequest request = controller.getRequest();
		HttpServletResponse response = controller.getResponse();
		
		if (!JfinalConfig.IS_PRODUCT_ENV) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
		}

		HttpSession session = request.getSession();
		Object admin = session.getAttribute("login_admin");
		if (admin == null && !inv.getActionKey().contains("login")) {
			try {
				response.sendRedirect(request.getContextPath()+"/admin");
			} catch (IOException e) {
				e.printStackTrace();
			}
			//inv.invoke();
		} else {
			inv.invoke();
		}
	}

}
