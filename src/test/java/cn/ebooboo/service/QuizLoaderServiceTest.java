package cn.ebooboo.service;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class QuizLoaderServiceTest {

	@Test
	@Ignore
	public void testLoad() {
		QuizLoaderService s = new QuizLoaderService();
		s.load("d:/temp/quiz.txt", 0);
	}

}
