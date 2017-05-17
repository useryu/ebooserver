package cn.ebooboo.controller;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.common.interceptor.LoginInterceptor;
import cn.ebooboo.model.BookResult;
import cn.ebooboo.model.EffectHourHist;
import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.model.User;
import cn.ebooboo.service.LoaderService;

public class QuizController extends FrontBaseController{

	public void index() {
		renderJson("test");
	}
	
	public void submitLevelPoint() {
		int point = super.getParaToInt("point",-1);
		String userId = super.getAttrForStr("userid");
		User user = User.dao.findById(userId);
		Integer seconds=this.convertToEffectHour(point);
		if(user==null) {
			user = new User();
			user.set("id",userId);
			user.set("point", point);
			user.save();
		} else {
			user.set("pre_point", user.getInt("point"));
			user.set("point", point);
			user.update();
		}
		EffectHourHist hist = new EffectHourHist();
		hist.setUserId(userId);
		hist.setDatetime(new Date());
		hist.setSource(EffectHourHist.SOURCE_FIRST_QUIZ);
		hist.setSeconds(seconds);
		hist.save();
		renderJson(1);
	}
	
	// 初始测试对的题目数转为初始有效时
	private Integer convertToEffectHour(int point) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void submitBookQuizPoint() {
		int point = super.getParaToInt("point",-1);
		int total = super.getParaToInt("total",-1);
		int bookId = super.getParaToInt("bookId",-1);
		String userId = (String)getRequest().getAttribute("userid");
		double throshold = PropKit.getInt("quiz_throshold");
		int isDone=point*0.1/total*1000>=throshold?1:0;//超过80%认为通过
		BookResult br = BookResult.dao.findFirst("select * from book_result br where user_id=? and book_id=?", userId, bookId);
		if(br==null) {
			br = new BookResult();
			br.setQuizIsDone(isDone);
			br.setUserId(userId);
			br.setBookId(bookId);
			br.save();
		} else {
			br.setQuizIsDone(isDone);
			br.update();
		}
		renderJson(br);
	}
	
	private List<Quiz> getQuiz(int chapterId) {
		List<Quiz> quizs = Quiz.dao.find("select * from quiz where chapter_id=? order by chapter_id,no", chapterId);
		for(Quiz q:quizs) {
			q.put("options", QuizOption.dao.find("select * from quiz_option where quiz_id=?", q.getId()));
		}
		return quizs;
	}
	
	public void getLevelQuiz() {
		renderJson(this.getQuiz(0));
	}
	
	public void getBookQuiz() {
		String bookId = super.getPara("bookId");
		bookId=StrKit.isBlank(bookId)?"0":bookId;
		List<Quiz> quizs = Quiz.dao.find("select * from quiz where chapter_id in (select id from chapter where book_id=?)  order by chapter_id,no", bookId);
		for(Quiz q:quizs) {
			q.put("options", QuizOption.dao.find("select * from quiz_option where quiz_id=?", q.getId()));
		}
		renderJson(quizs);
	}
	
	@Clear(LoginInterceptor.class)
	public void loadLevelQuiz() {
		String fileName = "d:/temp/quiz.txt";
		if(JfinalConfig.IS_PRODUCT_ENV) {
			fileName="/var/eboofiles/level_quiz.txt";
		}
		LoaderService.me.loadQuiz(fileName, 0);
		renderJson(1);
	}
}
