package cn.ebooboo;

import java.io.IOException;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import cn.ebooboo.controller.AdminController;
import cn.ebooboo.controller.BookController;
import cn.ebooboo.controller.LoginController;
import cn.ebooboo.controller.TestController;
import cn.ebooboo.controller.TunnelController;
import cn.ebooboo.controller.UserController;
import cn.ebooboo.model._MappingKit;
import cn.ebooboo.util.ModelScanner;

public class JfinalConfig extends JFinalConfig {
	
	public static boolean IS_PRODUCT_ENV=false;
	
	public void configConstant(Constants me) {
		String env = System.getenv("config_env");
		env = env==null?"prod":env;
		QCloud.setupSDK();
		IS_PRODUCT_ENV = "prod".equals(env);
		me.setDevMode(!IS_PRODUCT_ENV);
	}

	public void configRoute(Routes me) {
		me.add("/test", TestController.class);
		me.add("/user", UserController.class);
		me.add("/login", LoginController.class);
		me.add("/tunnel", TunnelController.class);
		me.add("/admin", AdminController.class);
		me.add("/book", BookController.class);
	}

	public void configEngine(Engine me) {
	}

	public void configPlugin(Plugins me) {
		String env = System.getenv("config_env");
		env = env==null?"prod":env;
		loadPropertyFile("/config/"+env+"/db.properties");
		DruidPlugin dp = new DruidPlugin(getProperty("hibernate.connection.url"),
				getProperty("hibernate.connection.username"), getProperty("hibernate.connection.password"),
				getProperty("hibernate.connection.driver_class"));
		me.add(dp);
//		dp.start();
//		ModelGenerator.getInstance().gen(dp.getDataSource());
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		me.add(arp);
		_MappingKit.mapping(arp);
//		try {
//			new ModelScanner().scan("cn.ebooboo.model", arp);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void configInterceptor(Interceptors me) {
	}

	public void configHandler(Handlers me) {
	}
}