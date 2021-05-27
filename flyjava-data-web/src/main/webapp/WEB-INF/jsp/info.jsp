<%@ page language="java" import="java.util.*,java.io.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	  <%
 //从linux 或者windows下读取配置文件
 	String pathWindow="E:/conf/flyjava/resource.properties";
 	String pathLinux="/conf/flyjava/resource.properties";
 	Properties pro = new Properties(); 
 	BufferedReader reader=null; 
	File file=new File(pathWindow);
	if(file.exists()){
		 reader=new BufferedReader(new FileReader(file));
	}
	else{
		File file2=new File(pathLinux);
		reader=new BufferedReader(new FileReader(file2));
	}
	 pro.load(reader);  
	 //通过key获取配置文件
	 String SSO_URL = pro.getProperty("SSO_URL"); 
	 String DATA_URL = pro.getProperty("DATA_URL"); 
	 //跳转到首页
	 String INDEX_URL= pro.getProperty("INDEX_URL"); 
	reader.close();
%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>测控研究室数据中心</title>
<meta name="description" content="东北农业大学测控研究室数据中心" />
<meta name="keywords" content="东北农业大学测控研究室数据中心，测控研究室，物联网 ，东北农业大学测控研究室， 农业物联网 ，农业物联网" />
<meta name="author" content="彭皖成" />

<!-- 适配 -->
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/font-awesome.css" rel="stylesheet">


<link href="/css/animate.css" rel="stylesheet">
<link href="/css/style.css" rel="stylesheet">



</head>

<body class="fixed-sidebar">
	<div id="wrapper">
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav" id="side-menu">
					<li class="nav-header">
						<div class="dropdown profile-element">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<span class="clear"> <span class="block m-t-xs"> <strong
										class="font-bold">${user.username}</strong> <b class="caret"></b>
								</span>
							</span>
							</a>
							<ul class="dropdown-menu animated fadeInRight m-t-xs">
								<li><a href="<%=DATA_URL%>/password.html">修改密码</a>
								</li>
								<li><a href="<%=DATA_URL%>/account.html">我的信息</a>
								</li>
								<li class="divider"></li>
								<li><a href="<%=SSO_URL%>/user/loginOut">安全退出</a>
								</li>
							</ul>
						</div> <!-- 折叠侧边栏后显示的文字 -->
						<div class="logo-element"></div>
					</li>

					<li ><a
						href="<%=DATA_URL%>/data/showUserHost"> <i
							class="fa fa-bar-chart" aria-hidden="true"></i> <span
							class="nav-label"> 我的设备</span>
					</a></li>

					<li><a href="<%=DATA_URL%>/device-manager.html">
							<i class="fa fa-cogs" aria-hidden="true"></i> <span
							class="nav-label"> 设备管理</span>
					</a></li>

					<li><a href="<%=DATA_URL%>/map.html"> <i
							class="fa fa-location-arrow" aria-hidden="true"></i> <span
							class="nav-label"> 设备分布</span>
					</a></li>

					<li>
						<a href="<%=DATA_URL%>/granary?productId=">
							 <i class="fa fa-location-arrow" aria-hidden="true"></i>
							 <span class="nav-label"> 粮仓监测</span>
						</a>
					</li>
					
					<li><a href="<%=DATA_URL%>/account.html"> <i
							class="fa fa-user" aria-hidden="true"></i> <span
							class="nav-label"> 账号管理</span>
					</a></li>

					<li class="active"><a href="<%=DATA_URL%>/info.html"> <i
							class="fa fa-info-circle" aria-hidden="true"></i> <span
							class="nav-label"> 版本信息</span>
					</a></li>
				</ul>

			</div>
		</nav>


		<!-- 内容区域 -->
		<div id="page-wrapper" class="gray-bg dashbard-1">
			<!-- 面包屑 -->
			<!--头部-->
			<div class="row wrapper border-bottom white-bg page-heading">
				<div class="col-lg-10">
					<h2>
						<a href="index.html" rel="home"><img
							src="/images/logo_black.png" width="194" alt="测控研究室"></a> 数据中心
					</h2>

				</div>
			</div>



			<div class="row  border-bottom white-bg dashboard-header">
				<div class="col-sm-12">
					 <div class="list-group">
						<a href="#" class="list-group-item active">
							<h1>版本信息</h1>
						</a>
						
						<a href="#" class="list-group-item">
							<h4>V1.8 -20181210</h4>
							<p class="list-group-item-text">实现了粮仓温湿度20个设备存储和展示，可以在设备管理中添加，在设备列表中进行展示
							<br>注意，这个版本的配置文件都外置了，需要把配置文件放在/conf/flyjava/resource.properties下</p>
						</a>
						<a href="#" class="list-group-item">
							<h4>V1.7 -20180312</h4>
							<p class="list-group-item-text">实现了Https域名访问，申请了阿里云的一年免费的CA证书。
							<br>注意，Echarts控件AJAX数据交互也要写成Https方式。其他的可以在Nginx做Http跳转Https。</p>
						</a>
						<a href="#" class="list-group-item">
							<h4>V1.6 -20180307-晚上</h4>
							<p class="list-group-item-text">实现了mysql数据库的迁移。历史查询支持20170101-20180301。</p>
						</a>
						<a href="#" class="list-group-item">
							<h4>V1.5 -20180305-晚上</h4>
							<p class="list-group-item-text">问题：查询实时数据和历史数据页面加载慢。<br/>分析：火狐浏览器调试网络加载发现加载echarts.js有2兆多，花费了很长时间！
							<br/>解决方法：1、使用Echarts在线定制生成echarts.min.js，只有438k！2、压缩css和js文件。</p>
						</a> 
						<a href="#" class="list-group-item">
							<h4>V1.4 -20180303-晚上</h4>
							<p class="list-group-item-text">问题：查询历史数据慢！<br/>分析:部署在阿里云的4台服务器使用公网IP进行数据交互，花费了一定的时间！<br/>
							解决办法：设置阿里云服务器内网VPC互联，意思就是使用阿里云的高速通道，实现内网IP数据交互。</p>
						</a>
						<a href="#" class="list-group-item">
							<h4>V1.3 -20180303</h4>
							<p class="list-group-item-text">问题：手机打开网站，出现了不适应。部分页面不能友好的显示。<br/>分析：应该是bootstrap适应性规则问题。
							<br/>解决方法：修改的Bootstrp.css在小于屏幕小于768px下的css属性，比如调整侧栏菜单的padding和字体大小等。</p>
						</a> 
						<a href="#" class="list-group-item">
							<h4>V1.2 -20180302</h4>
							<p class="list-group-item-text">今天正式借助阿里云的服务器测试上线，其中整个系统一共4台服务器，每台均是学生特惠购买的，
								配置为1核内存2G,带宽1m.测试历史数据查询时发现速度非常慢，与在实验室测试相差太远，怀疑是硬件和网络问题。
								我直接连mysql测试读取1年以内的数据都是在0.03S内。</p>
								
							<table class="table">
							<tr>
								<td width="200">服务器A-IP:47.93.204.221 </td>
								<td>
			          				作为Zokeeper服务器和Redis缓存服务器。
								<td>
								
							</tr>
							<tr>
								<td width="200">服务器B-IP:39.106.181.34</td>
								<td>作为MySQL数据库</td>
								
							</tr>
							<tr>
								<td width="200">服务器C-IP:39.107.234.81</td>
								<td>作为WEB服务器。有flyjava-data和flyjava-sso</td>
								
							</tr>
							<tr>
								<td width="200">服务器D-IP:47.94.101.182</td>
								<td>作为Nginx和WEB服务器，有flyjava-data-web和flyjava-sso-web</td>
								
							</tr>
						</table>
						</a> 
						<a href="#" class="list-group-item">
							<h4>V1.1 -20180130</h4>
							<p class="list-group-item-text">实现了数据的查询功能,实现了一年以内数据的快速查询.</p>
						</a> 
						<a href="#" class="list-group-item">
							<h4>V1.0 -20180124</h4>
							<p class="list-group-item-text">实现了数据的实时显示,实现了设备管理功能,实现了用户信息更改,实现了用户退出!</p>
						</a>
					</div>

				</div>
			</div>
		</div>
		<!-- 内容区域end -->


	</div>

	<!-- Mainly scripts -->
	<script src="/js/jquery-2.1.1.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
    <script src="/js/hplus.js" ></script>
    <script src="/js/plugins/pace/pace.min.js" ></script>

</body>
</html>
