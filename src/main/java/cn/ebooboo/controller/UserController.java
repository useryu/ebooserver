package cn.ebooboo.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;

import cn.ebooboo.model.Book;
import cn.ebooboo.model.BookResult;
import cn.ebooboo.model.User;

public class UserController extends FrontBaseController{

	public void index() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		LoginService service = new LoginService(request, response);		
		try {
			// 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
			UserInfo userInfo = service.check();
			
			// 获取会话成功，输出获得的用户信息			
			HashMap<String,Object> result = new HashMap<String,Object>();
			HashMap<String,Object> data = new HashMap<String,Object>();
			data.put("userInfo", userInfo);
			logger.info(userInfo.getUserid());
			User user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			if(user==null) {
				user = new User();
				user.set("id", userInfo.getUserid());
				user.setLevel(1);
				user.save();
				user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			}
			user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
			user.setEffectHour(user);
			data.put("user", user);

			Book book = Book.dao.findFirst("select * from book where id=?", user.getReadingBookId());
			if(book!=null) {
				BookResult br = BookResult.dao.findFirst("select * from book_result where book_id=?",book.getId());
				book.put("bookResult",br);
			}
			book=book==null?new Book():book;
			data.put("book", book);

			
			result.put("code", 0);
			result.put("message", "OK");
			result.put("data", data);			
			super.renderJson(result);		
		} catch (LoginServiceException e) {
			e.printStackTrace();
		}catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	
	public void submitSuggestion() {
		String recontent=super.getPara("recontent");
		String items=super.getPara("items");
		String from=super.getPara("from");
		logger.info("user suggestion:");
		logger.info(from);
		logger.info(recontent);
		logger.info(items);
		super.renderJson(items);
	}
	
	public void switchToBook() {
			String bookNoStr=super.getPara("bookNo");
		String[] strs = bookNoStr.split("\\.");
		if(strs.length==2) {
			int level = Integer.parseInt(strs[0]);
			int bookNo = Integer.parseInt(strs[1]);
			HttpServletResponse response = getResponse();
			HttpServletRequest request = getRequest();
			LoginService service = new LoginService(request, response);		
			try {
				// 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
				UserInfo userInfo = service.check();
				
				// 获取会话成功，输出获得的用户信息			
				HashMap<String,Object> result = new HashMap<String,Object>();
				HashMap<String,Object> data = new HashMap<String,Object>();
				data.put("userInfo", userInfo);
				logger.info(userInfo.getUserid());
				User user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
				if(user==null) {
					user = new User();
					user.set("id", userInfo.getUserid());
					user.save();
					user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
				}
				if(user.getName()==null) {
					user.setName(userInfo.getNickName());
					user.update();
				}
				user = User.dao.findFirst("select * from user where id=?", userInfo.getUserid());
				user.setEffectHour(user);
	
				Book book = Book.dao.findFirst("select * from book where level=? and no=?",level, bookNo);
				if(book!=null) {
					BookResult br = BookResult.dao.findFirst("select * from book_result where book_id=?", book.getId());
					br=br==null?new BookResult():br;
					data.put("bookResult",br);
					user.setReadingBookId(book.getId());
					user.update();
				}
				book=book==null?new Book():book;
				data.put("book", book);
	
				
				data.put("user", user);
				result.put("code", 0);
				result.put("message", "OK");
				result.put("data", data);
				super.renderJson(result);
			} catch (LoginServiceException e) {
				e.printStackTrace();
			}catch (ConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			
		}
	}

	
}
