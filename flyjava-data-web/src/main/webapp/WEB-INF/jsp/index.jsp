<%@ page language="java" import="java.util.*,java.io.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
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
              <a data-toggle="dropdown" class="dropdown-toggle" href="#" >
                <span class="clear"> 
                  <span class="block m-t-xs">
                    <strong class="font-bold">${user.username}</strong>
                    <b class="caret"></b>
                  </span> 
                </span>
              </a>
              <ul class="dropdown-menu animated fadeInRight m-t-xs">
                  <li>
                      <a href="<%=DATA_URL%>/password.html">修改密码</a>
                  </li>
                  <li>
                      <a href="<%=DATA_URL%>/account.html" >我的信息</a>
                  </li>
                  <li class="divider"></li>
                  <li>
                      <a href="<%=SSO_URL%>/user/loginOut">安全退出</a>
                  </li>
              </ul>
          </div>
          <!-- 折叠侧边栏后显示的文字 -->
          <div class="logo-element">
                          
          </div>
        </li>

        <li ><!--class="active"  -->
            <a href="<%=DATA_URL%>/data/showUserHost" >
                <i class="fa fa-bar-chart" aria-hidden="true"></i>
                <span class="nav-label"> 我的设备</span>
            </a>
        </li>

        <li>
            <a href="<%=DATA_URL%>/device-manager.html" >
            	<i class="fa fa-cogs" aria-hidden="true"></i>
                <span class="nav-label"> 设备管理</span>
            </a>
        </li>

        <li>
            <a href="<%=DATA_URL%>/map.html" >
            	<i class="fa fa-location-arrow" aria-hidden="true"></i>
                <span class="nav-label"> 设备分布</span>
            </a>
        </li>

		<li>
			<a href="<%=DATA_URL%>/granary?productId=">
				 <i class="fa fa-location-arrow" aria-hidden="true"></i>
				 <span class="nav-label"> 粮仓监测</span>
			</a>
		</li>
		
        <li>
            <a href="<%=DATA_URL%>/account.html" >
            	<i class="fa fa-user" aria-hidden="true"></i>
                <span class="nav-label"> 账号管理</span>
            </a>
        </li>

        <li>
            <a href="<%=DATA_URL%>/info.html" >
            	<i class="fa fa-info-circle" aria-hidden="true"></i>
                <span class="nav-label"> 版本信息</span>
            </a>
        </li>
      </ul>

    </div>
  </nav>


	<!-- 内容区域 -->
	<div id="page-wrapper" class="gray-bg dashbard-1">
    <!-- 面包屑 -->
    <!--头部-->
    <div class="row wrapper border-bottom white-bg page-heading">
      <div class="col-lg-10">
        <h2> <a href="index.html" rel="home"><img src="/images/logo_black.png" width="194" alt="测控研究室"></a>  数据中心</h2>
        
      </div>
    </div> 



    <div class="row  border-bottom white-bg dashboard-header">
      <div class="col-sm-6">
        <h2>欢迎${user.username} !</h2>
        <p>欢迎使用本系统！</p>
        
      </div>
    </div>
  </div>
	<!-- 内容区域end -->
		

    </div>

    <!-- Mainly scripts -->
    <script src="/js/jquery-2.1.1.min.js" ></script>
    <script src="/js/bootstrap.min.js" ></script>
    <script src="/js/plugins/metisMenu/jquery.metisMenu.js" ></script>
    <script src="/js/plugins/slimscroll/jquery.slimscroll.min.js" ></script>

	<!-- Custom and plugin javascript -->
    <script src="/js/hplus.js" ></script>
    <script src="/js/plugins/pace/pace.min.js" ></script>
   
</body>
</html>
