package cn.ebooboo.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import cn.ebooboo.model.Admin;
import cn.ebooboo.vo.AdminLoginResp;

public class ChapterController extends BaseAdminController {

	public void list() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();

		super.setAllowCrossDomain(response);
		JSONObject json = super.getReqJson(request, response);
		String username = json.getString("username");
		String password = json.getString("password");
		Admin admin = Admin.dao.findFirst("select * from admin where username=?", username);
		if (admin != null) {
			if (admin.getStr("password").equals(password)) {
				AdminLoginResp resp = new AdminLoginResp();
				resp.setCode(200);
				resp.setMsg("success");
				admin.set("password", "");// don't let password to client
				admin.put("avatar", "https://raw.githubusercontent.com/taylorchen709/markdown-images/master/vueadmin/user.png");
				resp.setUser(admin);
				request.getSession().setAttribute("login_admin", admin);
				renderJson(resp);
				return;
			}
		}

		AdminLoginResp resp = new AdminLoginResp();
		resp.setCode(500);
		resp.setMsg("用户名密码不对");
		renderJson(resp);
	}
}
