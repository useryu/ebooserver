package cn.ebooboo.controller;

import java.util.List;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.model.Assist;
import cn.ebooboo.service.LoaderService;

public class AssistController extends BaseController{

	public void index() {
		renderJson("test");
	}
	
	public void getBookAssist() {
		String bookId = super.getPara("bookId");
		String urlPrefix = PropKit.get("resourcePrefix");
		bookId=StrKit.isBlank(bookId)?"0":bookId;
		List<Assist> assist = Assist.dao.find("select a.id,a.chapter_id,a.word,a.english_explanation,a.example,concat('"+ urlPrefix
				+"',a.pic_url) as pic_url from assist a left join chapter c on a.chapter_id=c.id  where c.book_id=? order by c.no", bookId);
		renderJson(assist);
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
