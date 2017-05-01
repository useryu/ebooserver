package cn.ebooboo.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.common.interceptor.LoginInterceptor;
import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.model.User;
import cn.ebooboo.service.QuizLoaderService;

public class QuizController extends BaseController{

	public void index() {
		renderJson("test");
	}
	
	@Before(LoginInterceptor.class)
	public void submitLevelPoint() {
		int point = super.getParaToInt("point",-1);
		User user = User.dao.findById(getRequest().getAttribute("userid"));
		if(user==null) {
			user = new User();
			user.set("id",getRequest().getAttribute("userid"));
			user.set("point", point);
			user.save();
		} else {
			user.set("pre_point", user.getInt("point"));
			user.set("point", point);
			user.update();
		}
		renderJson(1);
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
	
	public void loadLevelQuiz() {
		String fileName = "d:/temp/quiz.txt";
		if(JfinalConfig.IS_PRODUCT_ENV) {
			fileName="/var/eboofiles/level_quiz.txt";
		}
		QuizLoaderService.me.load(fileName, 0);
		renderJson(1);
	}
}
