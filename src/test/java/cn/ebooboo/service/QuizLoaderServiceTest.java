package cn.ebooboo.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

public class QuizLoaderServiceTest {

	@Test
	@Ignore
	public void testLoad() {
		LoaderService s = new LoaderService();
		try {
			s.loadQuiz("d:/var/eboofiles/zip/1/1/1/test.txt",-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
//	@Ignore
	public void testReg() {
		String s = "﻿1.Who looks like a spaceman （谁看起来像宇航员）?";
		String s2 = "1.Who looks like a spaceman （谁看起来像宇航员）?";
		Pattern pattern = Pattern.compile("^[0-9]+.*");
		Matcher isNum = pattern.matcher(s.trim());
		System.out.println(isNum.matches());
	}

}
