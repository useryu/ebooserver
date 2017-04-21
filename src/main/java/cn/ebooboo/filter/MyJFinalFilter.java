package cn.ebooboo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.JFinalFilter;

import cn.ebooboo.JfinalConfig;

public class MyJFinalFilter extends JFinalFilter {
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		if(this.isOptionMethod(req,res)) {
			chain.doFilter(req, res);
		}else {
			super.doFilter(req, res, chain);
		}
	}

	private boolean isOptionMethod(ServletRequest req, ServletResponse res) {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		if(request.getMethod().equals("OPTIONS")) {
			if (!JfinalConfig.IS_PRODUCT_ENV) {
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
				response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
				return true;
			}
		}
		return false;
	}

}
