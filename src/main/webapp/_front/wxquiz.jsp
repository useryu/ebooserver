<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>英语级别测试</title>
    <meta charset="utf-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" href="http://demo.open.weixin.qq.com/jssdk/css/style.css">
    <script src="https://unpkg.com/vue"></script>
  </head>
  <body>
   <div class="wxapi_container" id="app">
    <div class="wxapi_index_container">
      <ul class="label_box lbox_close wxapi_index_list">
        <li class="label_item wxapi_index_item"></li>
      </ul>
    </div>
  </div>
  </body>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script type="text/javascript">

window.onload = function() {
		var vm = new Vue({
			el : '#app',
			data : {
				quizs : [],
				showAdvSearch : false
			},
			ready : function() {
				
			},
			methods : {

			}
		});
	};

	wx.config({
		debug : true,
		appId : '${appid}',
		timestamp : '${timestamp}',
		nonceStr : '${nonceStr}',
		signature : '${signature}',
		jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
				'onMenuShareAppMessage' ]
	});

	wx.ready(function() {
		var shareData = typeof (shareData) === 'undefined' ? {
			title : '易步阅读',
			desc : '做互联网上最懂第二语言习得的公共帐号。传播科学的第二语言习得理论；优秀学习资料推荐并定期提供公益外语培训',
			link : 'http://www.ebooboo.cn/',
			imgUrl : 'http://www.ebooboo.cn/img/qrcode.jpg'
		} : shareData;

		wx.onMenuShareAppMessage(shareData);
		wx.onMenuShareTimeline(shareData);
	});

	wx.error(function(res) {
		alert(res.errMsg);
	});
</script>
</html>
