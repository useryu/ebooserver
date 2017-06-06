package cn.ebooboo.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.ebooboo.common.ZipUtil;
import cn.ebooboo.model.Assist;
import cn.ebooboo.model.Audio;
import cn.ebooboo.model.Book;
import cn.ebooboo.model.Chapter;
import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.util.FileUtil;

public class PmpLoaderService {
	
	private static final Logger logger = Logger.getLogger(PmpLoaderService.class);
	
	public static final PmpLoaderService me = new PmpLoaderService();

	@Before(Tx.class)
	public void loadQuiz(String fileName) {
		List<String> lines = FileUtil.loadFile(fileName, "utf8");
		SortedMap<String,List<String>> quizs = new TreeMap<String, List<String>>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}});
		List<String> quizLines=null;
		for(int index=0;index<lines.size();index++) {
			String line=lines.get(index);
			if(StrKit.isBlank(line)) {
				continue;
			} else if(this.isTitleLine(line)){
				quizLines = new ArrayList<String>();
				quizLines.add(line);
			} else {
				if(quizLines!=null) {
					quizLines.add(line);
				}
			}		
		}
		
		Set<Entry<String, List<String>>> keys = quizs.entrySet();
		for(Iterator<Map.Entry<String, List<String>>> iter = keys.iterator(); iter.hasNext();){
		    Entry<String, List<String>> entry = iter.next();
		    String key = entry.getKey();
		    List<String> value = entry.getValue();
		    this.processQuiz(key,value);
		}
	}

	private void processQuiz(String no, List<String> valueLines) {
		StringBuilder sb = new StringBuilder();
		
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
		Pattern pattern = Pattern.compile("^[0-9]+.*");
		Matcher isNum = pattern.matcher(line.trim());
		return isNum.matches();
	}

	private boolean isOptionLine(String line) {
		Pattern pattern = Pattern.compile("^[a-zA-Z] .*");
		Matcher isNum = pattern.matcher(line.trim());
		return isNum.matches();
	}

}
