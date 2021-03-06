package cn.ebooboo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;

import cn.ebooboo.model.BookResult;
import cn.ebooboo.model.User;
import cn.ebooboo.util.VoBuilder;

public class AudioController extends FrontBaseController{

	public void playend() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		Integer audioId = super.getParaToInt("id");
		Integer bookId = super.getParaToInt("bookId");
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
			
			String isDone = "select is_done from audio_result where user_id=? and audio_id=?";
			Object doneResult = Db.queryFirst(isDone, userInfo.getUserid(), audioId);
			if(doneResult==null) {
				Db.update("insert into audio_result (audio_id,user_id,is_done) values (?, ? ,1)", audioId, userInfo.getUserid());
			}
			BookResult br = this.setBookResult(user, bookId);
			data.put("br", br);
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
	

	//if all audio in a book is done, set book_reasult
	private BookResult setBookResult(User user, Integer bookId) {
		String querySql = "select ar.is_done from audio a left join audio_result ar on ar.audio_id=a.id left join chapter c on a.chapter_id=c.id where ar.user_id=? and c.book_id=?";
		List<Integer> list = Db.query(querySql, user.getId(), bookId);
		boolean isAllDone=true;
		BookResult br = null;
		for(Integer i:list) {
			if(i==null || i==0) {
				isAllDone=false;
				break;
			}
		}
		if(isAllDone) {
			br = BookResult.dao.findFirst("select * from book_result br where br.user_id=? and book_id=? ", user.getId(), bookId);
			if(br==null) {
				br = new BookResult();
				br.setUserId(user.getId());
				br.setBookId(bookId);
				br.setAudioIsDone(1);
				br.save();
			} else {
				br.setAudioIsDone(1);
				br.update();
			}
		}
		return br;
	}

	public void list() {
		HttpServletResponse response = getResponse();
		String bookId = super.getPara("bookId");
		bookId=StrKit.isBlank(bookId)?"0":bookId;
		logger.info("get bookId:"+bookId+" to fetch audio list.");
		try {
			String urlPrefix = PropKit.get("resourcePrefix");
			// 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
			String query= "SELECT a.id, CONCAT('"+urlPrefix +"',a.url) as url ,c.chapter_name as chaptername,b.name as bookname "
					+ "FROM `audio` a left join chapter c on a.chapter_id=c.id left join book b on c.book_id=b.id where b.id=? order by c.no";
			// 获取会话成功，输出获得的用户信息			
			JSONObject result = new JSONObject();
			List<Record> list = Db.find(query, Integer.parseInt(bookId));
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
