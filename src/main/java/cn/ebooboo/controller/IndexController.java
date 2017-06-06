/**
 * Copyright (c) 2015-2016, Javen Zhou  (javenlife@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package cn.ebooboo.controller;

import javax.sql.DataSource;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.DbKit;

import cn.ebooboo.ModelGenerator;


public class IndexController extends Controller {
	public void index(){
		DataSource ds = DbKit.getConfig().getDataSource();
		new ModelGenerator().gen(ds );
		render("index.jsp");
	}
}
