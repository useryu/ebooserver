package cn.ebooboo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;

import cn.ebooboo.model.User;
import cn.ebooboo.util.VoBuilder;

public class AudioController extends BaseController{

	public void playend() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		LoginService service = new LoginService(request, response);		
		try {
			// 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
			UserInfo userInfo = service.check();
			
			// 获取会话成功，输出获得的用户信息			
			JSONObject result = new JSONObject();
			JSONObject data = new JSONObject();
			logger.info(userInfo.getUserid());
			User user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			if(user==null) {
				user = new User();
				user.set("id", userInfo.getUserid());
				user.save();
				user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			}
			Db.update("insert into audio_result (audio_id,user_id,is_done) values (?, ? ,1)", super.getParaToInt("id"), userInfo.getUserid());
			data.put("audio_id", super.getParaToInt("id"));
			result.put("code", 0);
			result.put("message", "OK");
			result.put("data", data);			
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(result.toString());			
		} catch (LoginServiceException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.renderNull();
	}
	
	public void list() {
		HttpServletResponse response = getResponse();
		try {
			// 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
			String query= "SELECT a.id,a.url,c.chapter_name as chaptername,b.name as bookname FROM `audio` a left join chapter c on a.chapter_id=c.id left join book b on c.book_id=b.id";
			// 获取会话成功，输出获得的用户信息			
			JSONObject result = new JSONObject();
			List<Record> list = Db.find(query);
			result.put("code", 0);
			result.put("message", "OK");
			result.put("list", VoBuilder.fetchColumn(list));			
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(result.toString());			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.renderNull();
	}
	
	
	
}
