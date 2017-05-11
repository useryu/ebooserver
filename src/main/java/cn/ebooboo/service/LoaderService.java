package cn.ebooboo.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;

import cn.ebooboo.common.ZipUtil;
import cn.ebooboo.model.Assist;
import cn.ebooboo.model.Audio;
import cn.ebooboo.model.Book;
import cn.ebooboo.model.Chapter;
import cn.ebooboo.model.Quiz;
import cn.ebooboo.model.QuizOption;
import cn.ebooboo.util.FileUtil;

public class LoaderService {
	
	private static final Logger logger = Logger.getLogger(LoaderService.class);
	
	public static final LoaderService me = new LoaderService();
	
	public class AudioFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".mp3");
		}
		
	}
	
	public class QuizFilter implements FileFilter{
		
		@Override
		public boolean accept(File pathname) {
			return pathname.getName().equalsIgnoreCase("test.txt");
		}
		
	}
	
	public class WordFilter implements FileFilter{
		
		@Override
		public boolean accept(File pathname) {
			return pathname.getName().equalsIgnoreCase("words.txt");
		}
		
	}
	
	@Before(Tx.class)
	public void loadAll(String fileName) throws IOException {
		File zipFile = new File(fileName);
		String[] no = zipFile.getName().split("\\.");
		if (no.length == 2) {
			int level = Integer.parseInt(no[0]);
			ZipUtil.unzip(fileName);
			File unzipDir = new File(zipFile.getParent(),no[0]);
			String targetDirPath = unzipDir.getPath();
			for(File dir:unzipDir.listFiles()) {
				String bookDir = dir.getName();
				int bookNo =Integer.parseInt(bookDir);
				for(File cDir:dir.listFiles()) {
					String cDirName = cDir.getName();
					int chapterNo = Integer.parseInt(cDirName);
					int bookId=this.createBookIfNotExist(level,bookNo,fileName);
					int chapterId=this.createChapterIfNotExist(bookId,chapterNo);
					String chapterDir = targetDirPath+File.separator+bookNo+File.separator+chapterNo+File.separator;
					this.loadChapterQuiz(chapterId, chapterDir);
					this.loadAudio(chapterId, chapterDir, bookNo, chapterNo);
					this.loadAssist(chapterId, chapterDir);
				}
			}
		} else {
			logger.info(fileName+" not a valid zip file");
		}
	}
	
	private void loadAssist(int chapterId, String targetDirPath) throws IOException {
		File dir = new File(targetDirPath);
		for(File file:dir.listFiles(new WordFilter())) {
			List<String> lines = FileUtil.loadFile(file.getPath(), "utf8");
			Assist assist = null;
			for(String line:lines) {
				if(StrKit.isBlank(line)) {
					continue;
				}
				int splitIndex = line.indexOf("-");
				if(splitIndex>0) {
					if(assist!=null) {
						assist.save();
					}
					assist=new Assist();
					String[] fields = line.split("-");
					assist.setWord(fields[0].trim());
					if(fields.length>1) {
						assist.setEnglishExplanation(fields[1].trim());
					} else {
						assist.setEnglishExplanation("");
					}
					assist.setChapterId(chapterId);
					assist.setPicUrl(this.processWordPic(assist.getWord(),targetDirPath));
				} else {
					if(assist!=null) {
						assist.setExample(line);
						assist.save();
						assist=null;
					} else {
						logger.info("unknow word line:"+line);
					}
				}
			}
			if(assist!=null) {
				assist.save();
			}
		}
	}

	private String processWordPic(String word, String targetDirPath) throws IOException {
		String picFilePath = targetDirPath+File.separator+"pic"+File.separator+word+".jpg";
		File picFile = new File(picFilePath);
		if(picFile.exists()) {
			String targetName="http://res.ebooboo.cn/resource?filename=";
			File distDir = picFile.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
			String distName = word+".jpg";
			File destFile = new File(distDir, distName);
			FileUtil.copyFile(picFile, destFile , true);
			return targetName+distName;
		} else {
			return null;
		}
	}

	private void loadAudio(int chapterId, String targetDirPath, int bookNo, int chapterNo) throws IOException {
		File dir = new File(targetDirPath);
		for(File file:dir.listFiles(new AudioFilter())) {
			Audio audio = new Audio();
			audio.setChapterId(chapterId);
			audio.setUrl(this.processAudioFile(file, chapterId, chapterNo));
			audio.save();
		}
	}

	private String processAudioFile(File file, int chapterId, int chapterNo) throws IOException {
		String targetName="http://res.ebooboo.cn/resource?filename=";
		File distDir = file.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
		String distName = "a-"+chapterId+"-"+chapterNo+".mp3";
		File destFile = new File(distDir, distName);
		FileUtil.copyFile(file, destFile , true);
		return targetName+distName;
	}

	private void loadChapterQuiz(int chapterId, String targetDirPath) {
		File dir = new File(targetDirPath);
		for(File file:dir.listFiles(new QuizFilter())) {
			this.loadQuiz(file.getPath(), chapterId);
		}
	}

	private int createChapterIfNotExist(int bookId, int chapterNo) {
		Chapter c = Chapter.dao.findFirst("select * from chapter where book_id=? and no=?", bookId, chapterNo);
		if(c==null) {
			c = new Chapter();
			c.setBookId(bookId);
			c.setChapterName(bookId+"."+chapterNo+"");
			c.setNo(chapterNo);
			c.setMemo("");
			c.save();
		}
		return c.getId();
	}

	private int createBookIfNotExist(int level, int bookNo, String fileName) {
		Book book = Book.dao.findFirst("select * from book where no=?", level+"."+bookNo+"");
		if(book==null) {
			book=new Book();
			book.setNo(bookNo);
			book.setName(level+"."+bookNo);
			book.setLevel(level);
			book.save();
		}
		return book.getId();
	}

	@Before(Tx.class)
	public void loadQuiz(String fileName, int chapter_id) {
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
				if(options==null) {
					throw new RuntimeException("发现异常"+fileName+" in line:"+line);
				}
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
