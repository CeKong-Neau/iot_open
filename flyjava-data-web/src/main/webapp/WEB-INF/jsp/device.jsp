<%@ page language="java" import="java.util.*,java.io.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<!-- 原来的2代的  -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- 新的 为了适应分页插件 发现只有 分页菜单能完美显示 -->
    <!-- <link href="/css/bootstrap.css" rel="stylesheet"> -->
    <!--自定义分页插件css  就是把上面的 关于分页插件的css样式复制过来了-->
    <link href="/css/mybootstrap-paginator.css" rel="stylesheet">
    
    
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

		<!--菜单-->
        <li class="active">
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
            <a href="<%=DATA_URL%>/account.html" >
            	<i class="fa fa-user" aria-hidden="true"></i>
                <span class="nav-label"> 账号管理</span>
            </a>
        </li>

		<li>
			<a href="<%=DATA_URL%>/granary?productId=">
				 <i class="fa fa-location-arrow" aria-hidden="true"></i>
				 <span class="nav-label"> 粮仓监测</span>
			</a>
		</li>
		
        <li>
            <a href="<%=DATA_URL%>/info.html" >
            	<i class="fa fa-info-circle" aria-hidden="true"></i>
                <span class="nav-label"> 版本信息</span>
            </a>
        </li><!--菜单结束-->
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


	<!--标题和搜索框-->
    <div class="row  white-bg dashboard-header">
    	
      	<div class="col-sm-8">
      		<h2>我的设备</h2>
     	</div>
     	<div class="col-sm-4">
	      	<div class="input-group">
				<input type="text" class="form-control bg-info" placeholder="关键词">
				<span class="input-group-btn">
				<button type="submit" class="btn btn-info"><span class="fa fa-search"></span></button>
				</span>
			</div>
	     </div>
      	
	</div>
	
	<!--设备列表-->
	<div class="row white-bg dashboard-header">
	    
		<!--一个设备模板  当为粮仓就跳转到粮仓页面-->
		<c:forEach items="${productList}" var="item">
			<c:choose>
			   <c:when test="${item.productCatId == '6'}">  
			     		<a href="<%=DATA_URL%>/granary?productId=${item.productId}" >
			   </c:when>
			   <c:otherwise> 
			 		 <a href="<%=DATA_URL%>/showData?productId=${item.productId }" >
			   </c:otherwise>
			</c:choose>
		
				<div class="col-sm-6">
						<div class="panel panel-success">
							<div class="panel-body bg-success">
								<h4 >产品名称:${item.productName}&nbsp;&nbsp;&nbsp;&nbsp;<em>No.${item.productId }</em></h4>
								<h4 class="text-success">创建时间: <fmt:formatDate value="${item.created}" pattern="yyyy-MM-dd HH:mm"/></h4>
							</div>
				    		
				    		<table class="table table-responsive">
				    				<tr>
				    					<td></td>
				    					<td><i class="fa fa-tint" aria-hidden="true"></i></td>
				    					<td><i class="fa fa-leaf" aria-hidden="true"></i></td>
				    					<td><i class="fa fa-cloud" aria-hidden="true"></i></td>
				    					<td><i class="fa fa-sun-o" aria-hidden="true"></i></td>
				    					<td><i class="fa fa-shield" aria-hidden="true"></i></td>
				    				</tr>
				    				<tr>
				    					<td></td>
				    					<td>--</td>
				    					<td>--</td>
				    					<td>--</td>
				    					<td>--</td>
				    					<td><c:if test="${item.status eq 1}"> <small class="text-success">工作中</small> </c:if>
				    						<c:if test="${item.status eq 2}"> <small class="text-danger">故障</small></c:if> </td>
				    				</tr>
				    		</table>
				    	</div>
				</div>
		   	</a>
		</c:forEach>
		<!--模板结束-->
	   
	 
	   	
	   	
	 </div><!--设备列表结束-->
	 
	<!--分页-->
	<div class="row white-bg">
	    <div class="col-sm-3">
		      	 <!--分页 左边-->
		</div>
		
		<div class="col-sm-9">
			<!--  分页开始-->
		     <!-- <ul class="pagination">
				<li><a href="#">&laquo;</a></li>
				<li><a href="#">1</a></li>
				<li><a href="#">2</a></li>
				<li><a href="#">3</a></li>
				<li><a href="#">4</a></li>
				<li><a href="#">5</a></li>
				<li><a href="#">&raquo;</a></li>
			</ul> -->
			<!-- 下面是控制分页控件 -->
			<div id="pagintor" class="pagination"></div>
			
			
			<!--分页结束  -->
		</div>
		
		
		
	 </div><!--分页结束-->
	 
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
    
    <!--分页插件  -->
    <script type="text/javascript" src="/js/bootstrap-paginator.min.js"></script>
    
    <script type="text/javascript">
		var options = {
			/* bootstrapMajorVersion:2, */ //对应的bootstrap版本
		    alignment:"andright",//右显示 设置控件的对齐方式，是个字符串， 允许的值用： left, center andright
		    currentPage: ${page},//当前页数
		    totalPages: ${totalPages},//总页数 注意不是总条数
		    shouldShowPage:true,//是否显示该按钮
		    itemTexts:function(type, page, current){//控制每个操作按钮的显示文字
		    	 switch (type) {
                    case "first":
                        return "首页";
                    case "prev":
                        return "上一页";
                    case "next":
                        return "下一页";
                    case "last":
                        return "末页";
                    case "page":
                        return page;
                }
		    
		    },
		    pageUrl: function(type, page, current){
		            if (page==current) {
		                return "javascript:void(0)";
		            } else {
		                return "<%=DATA_URL%>"+"/data/showUserProduct?page="+page+"&hostId="+${productList[0].hostId};
		            }
		        }
		}
		$("#pagintor").bootstrapPaginator(options);
    </script>
</body>
</html>