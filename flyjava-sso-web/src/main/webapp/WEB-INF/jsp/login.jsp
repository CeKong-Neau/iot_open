<%@ page language="java" import="java.util.*,java.io.*" import="java.util.ResourceBundle" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
 <%
 //从linux 或者windows下读取配置文件
 	String pathWindow="E:/conf/flyjava/resource.properties";
 	String pathLinux="/conf/flyjava/resource.properties";
 	Properties pro = new Properties(); 
 	BufferedReader reader=null; 
	File file=new File(pathLinux);
	if(file.exists()){
		 reader=new BufferedReader(new FileReader(file));
		 pro.load(reader);  
	}
	else{
		File file2=new File(pathWindow);
		reader=new BufferedReader(new FileReader(file2));
		pro.load(reader);  
	}
	 //通过key获取配置文件
	 String SSO_URL = pro.getProperty("SSO_URL"); 
	 String DATA_URL = pro.getProperty("DATA_URL"); 
	 //跳转到首页
	 String SUSSCESS_URL= pro.getProperty("SUSSCESS_URL"); 
	reader.close();
%> 



<!DOCTYPE HTML>
<html>

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>登录</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<meta name="author" content="" />

		<!-- Animate.css -->
		<link rel="stylesheet" href="/css/animate.css">
		<!-- Icomoon Icon Fonts-->
		<link rel="stylesheet" href="/css/icomoon.css">
		<!-- Themify Icons-->
		<link rel="stylesheet" href="/css/themify-icons.css">
		<!-- Bootstrap  -->
		<link rel="stylesheet" href="/css/bootstrap.css">

		<!-- Magnific Popup -->
		<link rel="stylesheet" href="/css/magnific-popup.css">

		<!-- Owl Carousel  -->
		<link rel="stylesheet" href="/css/owl.carousel.min.css">
		<link rel="stylesheet" href="/css/owl.theme.default.min.css">

		<!-- Theme style  -->
		<link rel="stylesheet" href="/css/style.css">

		<!-- Modernizr JS -->
		<script src="/js/modernizr-2.6.2.min.js"></script>
		<!-- FOR IE9 below -->
		<!--[if lt IE 9]>
	<script src="/js/respond.min.js"></script>
	<![endif]-->
<style type="text/css"> 

</style>
	</head>

	<body>

		<div class="gtco-loader"></div>

		<div id="page">

			<div class="page-inner">
				<nav class="gtco-nav" role="navigation">
					<div class="gtco-container">

						<div class="row">
							<div class="col-sm-4 col-xs-12">
								<div id="gtco-logo">
								
									<!-- logo图片 -->
									<a href="#" rel="home"><img src="/images/logo_white.png" width="194" alt="Antarctica"></a>
								</div>
							</div>
							<div class="col-xs-8 text-right menu-1">
								<ul>
									<li>
										<a href="ck.neau.edu.cn">网站首页</a>
									</li>
									<li>
										<a href="#">关于我们</a>
									</li>
									<li>
										<a href="#">数据中心</a>
									</li>
									<li>
										<a href="#">新闻博客</a>
									</li>
									<li>
										<a href="#">联系我们</a>
									</li>
									<!--<li class="btn-cta">
										<a href="#"><span>Get started</span></a>
									</li>-->
								</ul>
							</div>
						</div>

					</div>
				</nav>

				<header id="gtco-header" class="gtco-cover" role="banner" style="background-image: url(/images/bg-01.jpg)">
					<div class="overlay"></div>
					<div class="gtco-container">
						<div class="row">
							<div class="col-md-12 col-md-offset-0 text-left">

								<div class="row row-mt-15em">
									<div class="col-md-7 mt-text animate-box" data-animate-effect="fadeInUp">
										<span class="intro-text-small">Welcome to Measurement and Control Center !</span>
										<h1>欢迎登录测控研究室！</h1>
									</div>
									<div class="col-md-4 col-md-push-1 animate-box" data-animate-effect="fadeInRight">
										<div class="form-wrap">
											<div class="tab">
												<ul class="tab-menu">
													<li class="gtco-first">
														<a href="#" data-tab="signup">注册</a>
													</li>
													<li class="active gtco-second">
														<a href="#" data-tab="login">登陆</a>
													</li>
												</ul>
												<div class="tab-content">
													<!--注册-->
													<div class="tab-content-inner" data-content="signup">
														<form id="personRegForm" method="post" onsubmit="return false;">
															<div class="row form-group">
																<div class="col-md-12">
																	<label for="username">用户名</label>
																	<input type="text" class="form-control" id="regName" name="username" tabindex="1" 
																			onpaste="return false;" value=""
                                  											onfocus="if(this.value=='') this.value='';this.style.color='#333'"
                                  											onblur="if(this.value=='') {this.value='';this.style.color='#999999'}"/>
																</div>
																<!--onpaste  禁止粘贴
																	tabindex 设置键盘中的TAB键在控件中的移动顺序
																	onfocus 当 input 输入框获取焦点时执行一段 Javascript代码：
																	onblur  在元素失去焦点时触发。
																	autocomplete 根据用户输入值进行搜索和过滤，让用户快速找到并从预设值列表中选择。
																	ime-mode 用户输入号码的表单中，需要禁止用户输入中文、符号等
																-->
															</div>
															<div class="row form-group">
																<div class="col-md-12">
																	<label for="password">密码</label>
																	<input type="password" class="form-control" id="pwd" name="password"  tabindex="2"
                                  									 		style="ime-mode:disabled;" onpaste="return  false" autocomplete="off">
																</div>
															</div>
															<div class="row form-group">
																<div class="col-md-12">
																	<label for="password2">重复密码</label>
																	<input type="password" class="form-control" id="pwdRepeat" name="pwdRepeat" tabindex="3"
                                  											onpaste="return  false" autocomplete="off">
																</div>
															</div>
															<div class="row form-group">
																<div class="col-md-12">
																	<label for="phone">手机号</label>
																	<input type="tel" style="ime-mode:disabled;" class="form-control" id="phone" maxlength="11" name="phone" tabindex="4" autocomplete="off" >
																</div>
															</div>

															<div class="row form-group">
																<div class="col-md-12">
																	<input type="submit" class="btn btn-info" value="注册" id="registsubmit" tabindex="5"
																		onclick="REGISTER.reg();"> 
																</div>
															</div>
														</form>
													</div>
													
													<!--登陆-->
													<div class="tab-content-inner  active" data-content="login">
														<form id="formlogin" method="post" onsubmit="return false;">
															<div class="row form-group">
																<div class="col-md-12">
																	<label for="username">用户名</label>
																	<input type="text" class="form-control" id="loginname" name="username"  tabindex="1" autocomplete="off">
																</div>
															</div>
															<div class="row form-group">
																<div class="col-md-12">
																	<label for="password">密码</label>
																	<input type="password" class="form-control" id="nloginpwd" name="password" tabindex="2" autocomplete="off">
																</div>
															</div>

															<div class="row form-group">
																<div class="col-md-12">
																	<input type="submit" class="btn btn-info" value="登陆" id="loginsubmit" tabindex="3">
																</div>
															</div>
														</form>
													</div>

												</div>
											</div>
										</div>
									</div>
								</div>

							</div>
						</div>
					</div>
				</header>

			</div>

		</div>

		<!-- jQuery -->
		<script src="/js/jquery.min.js"></script>
		<!-- jQuery Easing -->
		<script src="/js/jquery.easing.1.3.js"></script>
		<!-- Bootstrap -->
		<script src="/js/bootstrap.min.js"></script>
		<!-- Waypoints -->
		<script src="/js/jquery.waypoints.min.js"></script>
		<!-- Carousel -->
		<script src="/js/owl.carousel.min.js"></script>
		<!-- countTo -->
		<script src="/js/jquery.countTo.js"></script>
		<!-- Magnific Popup -->
		<script src="/js/jquery.magnific-popup.min.js"></script>
		<script src="/js/magnific-popup-options.js"></script>
		<!-- Main -->
		<script src="/js/main.js"></script>
		<!-- 重要login.js -->
		<script src="/js/login.js"></script>

		<script type="text/javascript">
			/*  接受会调的url*/
			var redirectUrl = "${redirectUrl}";
			//获取从配置文件中读取的首页地址
			var indexUrl= "<%=SUSSCESS_URL%>";
			
		</script>

	</body>

</html>