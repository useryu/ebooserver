package cn.ebooboo;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.generator.Generator;

public class ModelGenerator {
	
	public static final ModelGenerator getInstance() {
		return new ModelGenerator();
	}
	
	public void gen(DataSource ds) {
		// base model 所使用的包名
		String baseModelPkg = "cn.ebooboo.model.base";
		// base model 文件保存路径
		String baseModelDir = "d:/temp/model/base";
		// model 所使用的包名
		String modelPkg = "cn.ebooboo.model";
		// model 文件保存路径
		String modelDir = baseModelDir + "/..";
		Generator gernerator = new Generator(ds , baseModelPkg, baseModelDir, modelPkg, modelDir);
		gernerator.generate();
	}
}
