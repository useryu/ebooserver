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
  </head>
  <body>
   <div class="wxapi_container" id="app">
    <div class="wxapi_index_container">
        <section class="label_item wxapi_index_item" v-for="quiz in quizs">
	        <h3>{{quiz.no}}.{{quiz.title}}</h3>
	        <div v-for="option in quiz.options" :id="quiz.id">
	        	<label>
	        	<input type="radio" v-on:click="radioChange(quiz.id,option.id)" :name="quiz.id" :id="option.id" :value="option.id" />
	        	{{option.text}}
	        	</label>
	        </div>
        </section>
        <br/>
        <a href="javascript:;" class="weui-btn weui-btn_primary"  v-on:click="submitAnswer">提交</a>
    </div>
  </div>
  </body>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script type="text/javascript" src="https://unpkg.com/vue"></script>
<script type="text/javascript">
Array.prototype.remove = function(s) {
    for (var i = 0; i < this.length; i++) {
        if (s == this[i])
            this.splice(i, 1);
    }
}
	window.onload = function() {
		var vm = new Vue({
			el : '#app',
			data : {
				quizs : [],
				mypointer:-1,
				myanswers:[]
			},
			created : function() {
				var that = this;
				var xmlhttp = new XMLHttpRequest();
				xmlhttp.open("GET", "${base}/quiz/getLevelQuiz", true);
				xmlhttp.onreadystatechange = function() {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						that.quizs = JSON.parse(xmlhttp.responseText);
					}
				}
				xmlhttp.send();
			},
			methods : {
			  radioChange: function(quizId,optionId) {
				  for(var a in this.myanswers){
					  var answer = this.myanswers[a];
					  if(answer.id==quizId){
						  this.myanswers.remove(answer);
						  break;
					  }
				  }
			  	this.myanswers.push({id:quizId,optionid:optionId})
			  },
			  submitAnswer: function() {
			    var pointer=0;
			    for(var i in this.myanswers){
			      var answer=this.myanswers[i]
			      for(var j in this.quizs){
			        var test = this.quizs[j]
			        if(answer.id==test.id){
			          if(answer.optionid==test.answer){
			            pointer++
			          }
			          break;
			        }
			      }
			    }
			    console.log(pointer);
			    alert('你做对的题目数量：'+(pointer-1));
			  }
			}
		});
	};
	if (wx) {
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
	}
	
	

</script>
</html>
