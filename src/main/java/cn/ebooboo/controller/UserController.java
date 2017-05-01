package cn.ebooboo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;

import cn.ebooboo.model.Book;
import cn.ebooboo.model.BookResult;
import cn.ebooboo.model.User;

public class UserController extends BaseController{

	public void index() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		LoginService service = new LoginService(request, response);		
		try {
			// 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
			UserInfo userInfo = service.check();
			
			// 获取会话成功，输出获得的用户信息			
			JSONObject result = new JSONObject();
			JSONObject data = new JSONObject();
			data.put("userInfo", new JSONObject(userInfo));
			logger.info(userInfo.getUserid());
			User user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			if(user==null) {
				user = new User();
				user.set("id", userInfo.getUserid());
				user.save();
				user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			}
			user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			data.put("user", new JSONObject(user));

			Book book = Book.dao.findFirst("select * from book where id=?", user.getReadingBookId());
			if(book!=null) {
				BookResult br = BookResult.dao.findFirst("select * from book_result where book_id=?",book.getId());
				book.put("bookResult",br);
			}
			book=book==null?new Book():book;
			data.put("book", new JSONObject(book));

			
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
	
	public void switchToBook() {
		String bookNo=super.getPara("bookNo");
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		LoginService service = new LoginService(request, response);		
		try {
			// 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
			UserInfo userInfo = service.check();
			
			// 获取会话成功，输出获得的用户信息			
			JSONObject result = new JSONObject();
			JSONObject data = new JSONObject();
			data.put("userInfo", new JSONObject(userInfo));
			logger.info(userInfo.getUserid());
			User user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			if(user==null) {
				user = new User();
				user.set("id", userInfo.getUserid());
				user.save();
				user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			}
			user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			data.put("user", new JSONObject(user));

			Book book = Book.dao.findFirst("select * from book where no=?", bookNo);
			if(book!=null) {
				BookResult br = BookResult.dao.findFirst("select * from book_result where book_id=?", book.getId());
				br=br==null?new BookResult():br;
				data.put("bookResult",br);
				user.setReadingBookId(book.getId());
				user.update();
			}
			book=book==null?new Book():book;
			data.put("book", new JSONObject(book));

			
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
	
}
