<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
    <!DOCTYPE HTML>
    <html lang="ch">
    <head>
    <meta charset="utf-8">
    <title>易步英语</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link rel='stylesheet' id='prettyphoto-css'  href="css/prettyPhoto.css" type='text/css' media='all'>
    <link href="css/fontello.css" type="text/css" rel="stylesheet">
    <!--[if lt IE 7]>
            <link href="css/fontello-ie7.css" type="text/css" rel="stylesheet">  
        <![endif]-->
    <!-- Google Web fonts -->
    <link href='http://fonts.googleapis.com/css?family=Quattrocento:400,700' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
    <style>
    body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
    }
    </style>
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    <!-- Favicon -->
    <link rel="shortcut icon" href="img/favicon.ico">
    <!-- JQuery -->
    <script type="text/javascript" src="js/jquery.js"></script>
    <!-- Load ScrollTo -->
    <script type="text/javascript" src="js/jquery.scrollTo-1.4.2-min.js"></script>
    <!-- Load LocalScroll -->
    <script type="text/javascript" src="js/jquery.localscroll-1.2.7-min.js"></script>
    <!-- prettyPhoto Initialization -->
    <script type="text/javascript" charset="utf-8">
          $(document).ready(function(){
            $("a[rel^='prettyPhoto']").prettyPhoto();
          });
        </script>
    </head>
    <body>
    <!--******************** NAVBAR ********************-->
    <div class="navbar-wrapper">
      <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
          <div class="container">
            <!-- Responsive Navbar Part 1: Button for triggering responsive navbar (not covered in tutorial). Include responsive CSS to utilize. -->
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </a>
            <h1 class="brand"><a href="#top">易步英语</a></h1>
            <!-- Responsive Navbar Part 2: Place all navbar contents you want collapsed withing .navbar-collapse.collapse. -->
            <nav class="pull-right nav-collapse collapse">
              <ul id="menu-main" class="nav">
              <!-- 
                <li><a title="portfolio" href="#portfolio">Portfolio</a></li>
                <li><a title="services" href="#services">Services</a></li>
                <li><a title="news" href="#news">News</a></li>
                <li><a title="team" href="#team">Team</a></li>
               -->
                <li><a title="contact" href="#contact">关于我们</a></li>
              </ul>
            </nav>
          </div>
          <!-- /.container -->
        </div>
        <!-- /.navbar-inner -->
      </div>
      <!-- /.navbar -->
    </div>
    <!-- /.navbar-wrapper -->
    <div id="top"></div>
    <!--******************** Services Section ********************-->
    <section id="services" class="single-page scrollblock">
      <div class="container">
        <div class="align"><img alt="" src="img/qrcode.jpg"></div>
        <h1>微信扫码关注</h1>
        <!-- Four columns -->
        <div class="row">
          <div class="span3">
            <div class="align"> <i class="icon-desktop sev_icon"></i> </div>
            <h2>微信公共帐号</h2>
            <p>做互联网上最懂第二语言习得的公共帐号。传播科学的第二语言习得理论；优秀学习资料推荐并定期提供公益外语培训。</p>
          </div>
          <!-- /.span3 -->
          <div class="span3">
            <div class="align"> <i class="icon-vector sev_icon"></i> </div>
            <h2>线下活动</h2>
            <p>每周的线下英语活动，做互联网上最懂第二语言习得的公共帐号。传播科学的第二语言习得理论；优秀学习资料推荐并定期提供公益外语培训。</p>
          </div>
          <!-- /.span3 -->
          <div class="span3">
            <div class="align"> <i class="icon-basket sev_icon"></i> </div>
            <h2>培训</h2>
            <p>最高效的培训，做互联网上最懂第二语言习得的公共帐号。传播科学的第二语言习得理论；优秀学习资料推荐并定期提供公益外语培训。</p>
          </div>
          <!-- /.span3 -->
          <div class="span3">
            <div class="align"> <i class="icon-mobile-1 sev_icon"></i> </div>
            <h2>反馈</h2>
            <p>提供及时反馈，做互联网上最懂第二语言习得的公共帐号。传播科学的第二语言习得理论；优秀学习资料推荐并定期提供公益外语培训。</p>
          </div>
          <!-- /.span3 -->
        </div>
        <!-- /.row -->
      </div>
      <!-- /.container -->
    </section>
    <hr>
    <div class="footer-wrapper">
      <div class="container">
        <footer>
          <small>&copy; 2017 深圳市声色教育有限公司 . All rights reserved.</small>
        </footer>
      </div>
      <!-- ./container -->
    </div>
    <!-- Loading the javaScript at the end of the page -->
    <script type="text/javascript" src="js/bootstrap.js"></script>
    <script type="text/javascript" src="js/jquery.prettyPhoto.js"></script>
    <script type="text/javascript" src="js/site.js"></script>
    
    <!--ANALYTICS CODE-->
	<script type="text/javascript">
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-29231762-1']);
	  _gaq.push(['_setDomainName', 'dzyngiri.com']);
	  _gaq.push(['_trackPageview']);
	
	  (function() {
		var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	</script>
    </body>
    </html>

