/**
 * Copyright (c) 2015-2016, Javen Zhou  (javenlife@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package cn.ebooboo.controller;

import com.jfinal.core.Controller;


public class IndexController extends Controller {
	public void index(){
		render("index.jsp");
	}
}
