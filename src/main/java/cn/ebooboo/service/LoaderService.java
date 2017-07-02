package cn.ebooboo.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.serializer.FilterUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.FileKit;
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

public class LoaderService {
	
	private static final Logger logger = Logger.getLogger(LoaderService.class);
	
	public static final LoaderService me = new LoaderService();
	
	public class AudioFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			String name = pathname.getName();
			int dotIndex = name.lastIndexOf(".");
			if(dotIndex>0) {
				String sufix = name.substring(dotIndex);
				return sufix.equalsIgnoreCase(".mp3");
			} else {
				return false;
			}
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
	public void removeAllInLevel(int level) {
		List<Integer> bookIds = Db.query("select id from book where level=?",level);
		this.removeAudioFile(level);
		for(Integer bookId:bookIds) {
			List<Integer> assistIds = Db.query("select id from assist where chapter_id in (select id from chapter where book_id=?)", bookId);
			this.removePicFile(assistIds);
			Db.update("delete from assist where chapter_id in (select id from chapter where book_id=?)", bookId);
			Db.update("delete from audio where chapter_id in (select id from chapter where book_id=?)", bookId);
			Db.update("delete from quiz_option where quiz_id in ( select id from quiz where chapter_id in (select id from chapter where book_id=?))", bookId);
			Db.update("delete from quiz where chapter_id in (select id from chapter where book_id=?)", bookId);
			Db.update("delete from chapter where book_id=?", bookId);
			Db.update("delete from book where level=?", level);
		}
	}
	
	private void removeAudioFile(int level) {
		File dir = new File(PropKit.get("file_download_path"));
		final String startPattern="a-"+level+"-";
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.getName().startsWith(startPattern) && f.getName().endsWith(".mp3");
			}
			
		});
		if(files!=null) {
			for(File f:files) {
				if(f.exists())f.delete();
			}
		}
	}

	private void removePicFile(List<Integer> assistIds) {
		for(int assistId:assistIds) {
			File f = new File(PropKit.get("file_download_path"), assistId+".jpg");
			if(f.exists())f.delete();
		}
	}

	@Before(Tx.class)
	public void loadAll(String fileName, String level) throws IOException {
		File sourceDir = new File(fileName);
		File levelDir = new File(sourceDir,level+"");
		for(File bookHoldDir:levelDir.listFiles()) {
			String bookDirName = bookHoldDir.getName();
			int bookNo =Integer.parseInt(bookDirName);
			logger.info("loading bookno:"+bookNo);
			for(File chapterHoldDir:bookHoldDir.listFiles()) {
				String chapterDirName = chapterHoldDir.getName();
				int chapterNo = Integer.parseInt(chapterDirName);
				Book book=this.createBookIfNotExist(Integer.parseInt(level),bookNo,fileName);
				int bookId= book.getId();
				int chapterId=this.createChapterIfNotExist(bookId,chapterNo);
				this.loadChapterQuiz(chapterId, chapterHoldDir.getPath());
				int effectSecond = this.loadAudio(chapterId, chapterHoldDir.getPath(), Integer.parseInt(level), bookNo, chapterNo, bookId);
				book.setEffectSecond(effectSecond);
				book.update();
				//this.loadAssist(chapterId, chapterHoldDir.getPath());
			}
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
						assist.setPicUrl(this.processWordPic(assist.getId(), assist.getWord(),targetDirPath));
						assist.update();
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
				} else {
					if(assist!=null) {
						assist.setExample(line);
						assist.save();
						assist.setPicUrl(this.processWordPic(assist.getId(), assist.getWord(),targetDirPath));
						assist.update();
						assist=null;
					} else {
						logger.info("unknow word line:"+line);
					}
				}
			}
			if(assist!=null) {
				assist.save();
				assist.setPicUrl(this.processWordPic(assist.getId(), assist.getWord(),targetDirPath));
				assist.update();
			}
		}
	}

	private String processWordPic(int id, final String word, String targetDirPath){
		String picFilePath = targetDirPath+File.separator+"pic"+File.separator;
		File dir = new File(picFilePath);
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().startsWith(word);
			}});
		
		if(files!=null && files.length>0) {
			File picFile = files[0];
			File distDir = new File(PropKit.get("file_download_path"));
			String distName = id+".jpg";
			File distFile = new File(distDir, distName);
			BufferedImage source;
			try {
				source = ImageIO.read(picFile);
				ImageIO.write(source, "JPG", distFile);
			} catch (Exception e) {
				e.printStackTrace();
				distName=null;
			}
			return distName;
		} else {
			return null;
		}
	}

	private int loadAudio(int chapterId, String targetDirPath, int level, int bookNo, int chapterNo, int bookId) throws IOException {
		File dir = new File(targetDirPath);
		int index=1;
		int totalEffectSecond=0;
		for(File file:dir.listFiles(new AudioFilter())) {
			Audio audio = new Audio();
			audio.setChapterId(chapterId);
			audio.setUrl(this.processAudioFile(file, level, bookNo, chapterNo, index, bookId));
			audio.save();
			index++;
			//totalEffectSecond+=FileUtil.getMp3TrackLength(file);
		}
		return totalEffectSecond;
	}

	private String processAudioFile(File file, int level, int bookNo, int chapterNo, int index, int bookId) throws IOException {
		File distDir = new File(PropKit.get("file_download_path"));
		String distName = "a-"+level+"-"+bookNo+"-"+chapterNo+"_"+index+".mp3";
		File destFile = new File(distDir, distName);
		FileUtil.copyFile(file, destFile , true);
		String chapterName = file.getName().substring(0, file.getName().length()-4);
		Db.update("update book set name=? where level=? and no=?", chapterName, level, bookNo);
		Db.update("update chapter set chapter_name=? where book_id=? and no=?", chapterName, bookId, chapterNo);
		return distName;
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

	private Book createBookIfNotExist(int level, int bookNo, String fileName) {
		Book book = Book.dao.findFirst("select * from book where level=? and no=?", level+"", bookNo+"");
		if(book==null) {
			book=new Book();
			book.setNo(bookNo);
			book.setName(level+"."+bookNo);
			book.setLevel(level);
			book.save();
		}
		return book;
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

	public void getEffect() {
		for(int level=1;level<=10;level++) {
			File levelDir = new File(PropKit.get("res_load_path"),level+"");
			StringBuffer updateEffectSqlBf = new StringBuffer();
			updateEffectSqlBf.append("update book set effect_second=0 where level="+level+";"+System.lineSeparator());
			for(File bookHoldDir:levelDir.listFiles()) {
				String bookDirName = bookHoldDir.getName();
				int bookNo =Integer.parseInt(bookDirName);
				logger.info("loading bookno:"+bookNo);
				for(File chapterHoldDir:bookHoldDir.listFiles()) {
					String chapterDirName = chapterHoldDir.getName();
					File[] audioFile = chapterHoldDir.listFiles(new AudioFilter());
					if(audioFile.length>0) {
						File mp3File = audioFile[0];
						int effectSecond = FileUtil.getMp3TrackLength(mp3File);
						updateEffectSqlBf.append("update book set effect_second=effect_second+"+effectSecond+" where level="+level+" and no="+bookNo+";"+System.lineSeparator());
					}
				}
			}
			File outPutFile=new File(levelDir.getParent(),level+".sql");
			Charset utf8 = Charset.forName("utf8");
			try {
				FileUtil.writeStringToFile(outPutFile, updateEffectSqlBf.toString(), utf8 , false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
