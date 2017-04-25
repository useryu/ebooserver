package cn.ebooboo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.util.FileUtil;

public class QuizLoaderService {
	
	public static final QuizLoaderService me = new QuizLoaderService();
	
	@Before(Tx.class)
	public void load(String fileName, int chapter_id) {
		List<String> lines = FileUtil.loadFile(fileName, "utf8");
		Quiz q = null;
		List<QuizOption> options=null;
		for(int index=0;index<lines.size();index++) {
			String line=lines.get(index);
			if(StrKit.isBlank(line)) {
				continue;
			} else if(this.isTitleLine(line)){
				int docIndex = line.indexOf(".");
				q=new Quiz();
				q.set("no", Integer.parseInt(line.substring(0, docIndex).trim()));
				q.set("title", line.substring(docIndex+1).trim());
				q.set("chapter_id", chapter_id);
				options = new ArrayList<QuizOption>();
			} else if(this.isOptionLine(line)) {
				options.add(this.genOption(line));
			} else if(this.isAnswerLine(line)) {
				if(q!=null && options!=null && options.size()>0) {
					Integer answer=-1;
					q.set("answer", answer);
					q.save();
					for(int i=0;i<options.size();i++) {
						QuizOption o = options.get(i);
						o.set("quiz_id", q.getId());
						o.save();
						if(o.getStr("no").equalsIgnoreCase(line.trim())) {
							answer=o.getId();
						}
						o.remove("no");
					}
					q.set("answer", answer);
					q.update();
					
					q=null;
					options=null;
				} else {
					throw new RuntimeException("发现异常数据");
				}
			}		
		}
	}

	private QuizOption genOption(String line) {
		QuizOption o = new QuizOption();
		int beginIndex = line.trim().indexOf(" ");
		o.set("text", line.trim().substring(beginIndex).trim());
		o.put("no", line.trim().substring(0, beginIndex).trim());
		return o;
	}

	private boolean isAnswerLine(String line) {
		return line.trim().length()==1;
	}

	private boolean isTitleLine(String line) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(line.trim().charAt(0)+"");
		return isNum.matches();
	}

	private boolean isOptionLine(String line) {
		Pattern pattern = Pattern.compile("^[a-zA-Z] .*");
		Matcher isNum = pattern.matcher(line.trim());
		return isNum.matches();
	}

}
