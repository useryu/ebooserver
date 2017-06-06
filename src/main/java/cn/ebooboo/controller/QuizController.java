package cn.ebooboo.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

import cn.ebooboo.JfinalConfig;
import cn.ebooboo.common.interceptor.LoginInterceptor;
import cn.ebooboo.model.BookResult;
import cn.ebooboo.model.EffectHourHist;
import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.model.User;
import cn.ebooboo.service.LoaderService;

public class QuizController extends FrontBaseController{
	

	public final static int [][] adultLevelPoint= {
						{1,10}
						,{11,11}
						,{12,12}
						,{13,15}
						,{16,17}
						,{18,19}
						,{20,21}
						,{22,22}
						,{23,23}
						,{24,25}
						};
	
	public final static int [][] childLevelPoint= {
			{1,9}
			,{10,11}
			,{12,15}
			,{16,17}
			,{18,18}
			,{19,20}
			,{21,21}
			,{22,22}
			,{23,23}
			,{24,25}
	};

	public void index() {
		renderJson("test");
	}
	
	public void submitLevelPoint() {
		int point = super.getParaToInt("point",-1);
		String userId = super.getPara("userid");
		String adult = super.getPara("adult");
		User user = User.dao.findById(userId);
		if(user==null) {
			user = new User();
			user.set("id",userId);
			user.set("point", point);
			user.save();
		} else {
			user.set("pre_point", user.getInt("point"));
			user.set("point", point);
			user.setAdult(Integer.parseInt(adult));
			user.update();
		}
		Integer seconds = this.convertToEffectHour(point,user.getAdult()==1);
		EffectHourHist hist = new EffectHourHist();
		hist.setUserId(userId);
		hist.setDatetime(new Date());
		hist.setSource(EffectHourHist.SOURCE_FIRST_QUIZ);
		hist.setSeconds(seconds);
		hist.save();
		renderJson(seconds);
	}
	
	// 初始测试对的题目数转为初始有效时
	/**
	 * 
	 * @param point
	 * @return
	 */
	private Integer convertToEffectHour(int point, boolean isAdult) {
		String sql = "select sum(effect_second) from book where level<=?";
		int level=1;
		if(isAdult) {
			level=this.convertAdultPointToLevel(point);
		}else {
			level=this.convertChildPointToLevel(point);
		}
		BigDecimal effectSeconds = Db.queryFirst(sql, level);
		return effectSeconds.intValue();
	}

	/**
	 * 对的题目数转为级别
正确题数   易步级别
1-10        1
11          2
12          3
13-15	    4
16-17       5
18-19       6
20-21	    7
22          8
23          9
24-25       10
	 * @param point
	 * @return
	 */
	private int convertAdultPointToLevel(int point) {
		int level=1;
		for(int i=0;i<10;i++) {
			if(point>=adultLevelPoint[i][0] && point<=adultLevelPoint[i][1]) {
				level = i+1;
				break;
			}
		}
		return level;
	}
	
	private int convertChildPointToLevel(int point) {
		int level=1;
		for(int i=0;i<10;i++) {
			if(point>=childLevelPoint[i][0] && point<=childLevelPoint[i][1]) {
				level = i+1;
				break;
			}
		}
		return level;
	}

	public void submitBookQuizPoint() {
		int point = super.getParaToInt("point",-1);
		int total = super.getParaToInt("total",-1);
		int bookId = super.getParaToInt("bookId",-1);
		String userId = (String)getRequest().getAttribute("userid");
		double throshold = PropKit.getInt("quiz_throshold");
		int isDone=point*0.1/total*1000>=throshold?1:0;//超过throshold认为通过
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

		User user = User.dao.findById(userId);
		BigDecimal levelTotalHour = Db.queryFirst("select IFNULL(sum(effect_second),0) from book where level=?", user.getLevel());
		BigDecimal passedLevelTotalHour = Db.queryFirst("select IFNULL(sum(effect_second),0) from book b left join book_result br on b.id=br.book_id where br.quiz_is_done=1 and level=? and br.user_id=?", user.getLevel(), userId);
		logger.info(passedLevelTotalHour+","+levelTotalHour+","+PropKit.getInt("level_throshold"));
		int ifUpgradeToNextLevel=0;
		if(passedLevelTotalHour!=null && passedLevelTotalHour.intValue()>0) {
			double levelThroshold = PropKit.getInt("level_throshold");
			ifUpgradeToNextLevel=passedLevelTotalHour.intValue()*0.1/levelTotalHour.intValue()*1000>=levelThroshold?1:0;//超过throshold认为通过
		}
		//看是不是可以升级了
		br.put("ifUpgradeToNextLevel",ifUpgradeToNextLevel);
		br.put("levelTotalHour",levelTotalHour==null?0:levelTotalHour.doubleValue()/360);
		br.put("passedLevelTotalHour",passedLevelTotalHour==null?0:passedLevelTotalHour.doubleValue()/360);
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
	
	public void getChildLevelQuiz() {
		renderJson(this.getQuiz(-1));
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
		String fileName = "d:/temp/level_quiz.txt";
		if(JfinalConfig.IS_PRODUCT_ENV) {
			fileName="/var/eboofiles/level_quiz.txt";
		}
		LoaderService.me.loadQuiz(fileName, 0);
		renderJson(1);
	}
	
	@Clear(LoginInterceptor.class)
	public void loadLevelQuizForChild() {
		String fileName = "d:/temp/child_level_quiz.txt";
		if(JfinalConfig.IS_PRODUCT_ENV) {
			fileName="/var/eboofiles/child_level_quiz.txt";
		}
		LoaderService.me.loadQuiz(fileName, -1);
		renderJson(1);
	}
}
