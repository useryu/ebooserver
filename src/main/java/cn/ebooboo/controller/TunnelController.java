package cn.ebooboo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.jfinal.core.Controller;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.tunnel.TunnelHandleOptions;
import com.qcloud.weapp.tunnel.TunnelService;

import cn.ebooboo.ChatTunnelHandler;

public class TunnelController extends Controller {

	public void index() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();

		// 创建信道服务处理信道相关请求
		TunnelService tunnelService = new TunnelService(request, response);

		try {
			// 配置是可选的，配置 CheckLogin 为 true 的话，会在隧道建立之前获取用户信息，以便业务将隧道和用户关联起来
			TunnelHandleOptions options = new TunnelHandleOptions();
			options.setCheckLogin(true);

			// 需要实现信道处理器，ChatTunnelHandler 是一个实现的范例
			tunnelService.handle(new ChatTunnelHandler(), options);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		super.renderNull();
	}

}
