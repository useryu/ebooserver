package cn.ebooboo.controller;

import java.util.List;

import com.jfinal.kit.StrKit;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.service.LoaderService;

public class AssistController extends BaseController{

	public void index() {
		renderJson("test");
	}
	
	public void getBookAssist() {
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
		LoaderService.me.loadQuiz(fileName, 0);
		renderJson(1);
	}
}
