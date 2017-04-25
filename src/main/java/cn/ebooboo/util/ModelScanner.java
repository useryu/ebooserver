package cn.ebooboo.util;

import java.io.IOException;
import java.util.List;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;


public class ModelScanner {

	
	public void scan(String modelPackage,ActiveRecordPlugin arp) throws IOException, ClassNotFoundException {

		PackageScanner scan = new ClasspathPackageScanner(modelPackage);
		List<String> classes = scan.getFullyQualifiedClassNameList();
		for(String c:classes) {
			Class candidate = Class.forName(c);
			if(candidate.isAnnotationPresent(Model.class)) {
				arp.addMapping(CamelCaseUtils.toUnderlineName(candidate.getName()), candidate);
			}
		}
	}

}
