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
                      <a href="<%=DATA_URL%>"+"u.cn/account.html" >我的信息</a>
                  </li>
                  <li class="divider"></li>
                  <li>
                      <a href="<%=SSO_URL%>/user/loginOut">安全退出</a>
                  </li>
              </ul>
          </div>
          <!-- 折叠侧边栏后显示的文字 -->
          <div class="logo-element">
              后台
          </div>
        </li>

         <li >
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

					<li class="active">
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
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="col-lg-10">
                    <h2>修改我的个人信息</h2>
                    <ol class="breadcrumb">
                    </ol>
                </div>
            </div>
            
            <!-- 表格 -->
            <div class="wrapper wrapper-content animated fadeInRight">
                
                <div class="row">
                    <div class="col-lg-12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>修改我的个人信息 <small></small></h5>
                            </div>
                            <div class="ibox-content">
    
							    <!-- 表单 -->
							    <form  id="updateForm" class="form-horizontal" method="post" onsubmit="return false;">
							        
							        <!-- 姓名 -->
							        <div class="form-group">
							        	 <input type="text" class="hidden" id="id" name="id"  value="${user.id}">
							            <label class="col-sm-2 control-label">姓名</label>
							            <div class="col-sm-5">
							                <input type="text" id="username" name="username" maxlength="20" class="form-control" placeholder=""  value="${user.username}" 
							                		disabled="disabled" autocomplete="off">
							            </div>
							        </div>
							        <div class="hr-line-dashed"></div>
							        
							        <!-- 邮箱 -->
							        <div class="form-group">
							            <label class="col-sm-2 control-label">邮箱</label>
							            <div   class="col-sm-5">
							                <input type="email" id="email" name="email"  class="form-control" placeholder="" value="${user.email}" 
							                 		tabindex="2" autocomplete="off">
							            </div>
							        </div>
							        <div class="hr-line-dashed"></div>
							        
							        <!-- 联系方式-->
							        <div class="form-group">
							            <label class="col-sm-2 control-label">手机号</label>
							            <div class="col-sm-5">
							                <input type="tel" id="phone" name="phone" class="form-control" placeholder="" maxlength="11" value="${user.phone}" 
							                			tabindex="3" autocomplete="off">
							            </div>
							        </div>
							        <div class="hr-line-dashed"></div>
							        
							        <div class="form-group">
							            <div class="col-sm-4 col-sm-offset-2">
							                <button class="btn btn-primary" type="submit" id="updatesubmit" tabindex="4" onclick="UPDATE.update();">确认更新</button>
							            </div>
							        </div>
							       
							    </form>
							    <!-- 表单end -->
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            <!-- 表格end -->
            

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


    <script>

	/*更新用户信息
	用户名必须有,不可以重复
	手机号必须有,可以重复
	邮箱可以没有
	跨服务器请求 必须用JSONP
	*/
	var UPDATE={
	      param:{
	            //单点登录系统的url
	            surl:"<%=SSO_URL%>"
	      },
	      inputcheck:function(){
	                  //不能为空检查 用户名不支持修改
	                  /* if ($("#username").val() == "") {
	                        alert("用户名不能为空");
	                        $("#username").focus();
	                        return false;
	                  } */
	                  if ($("#phone").val() == "") {
	                        alert("手机号不能为空");
	                        $("#phone").focus();
	                        return false;
	                  }
	                  //手机号码有效性 正则表达式验证
	                  var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
					  if(!myreg.test($("#phone").val())) 
					  { 
					      alert('请输入有效的手机号码！'); 
					      $("#phone").focus();
					      return false;
	    			  }
	    			  //如果邮箱不为空那就进行邮箱 格式验证
	    			  //为空就不验证
	    			  if ($("#email").val() != "") {
	    				  var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
	    				  if (!reg.test($("#email").val())) { //正则验证不通过，格式不对
	    					  alert("请输入有效的邮箱!");
	    					  $("#email").focus();
	    					  return false;
	    				 } 
	    			  }
	    			  return true;
	      },
	      //现在就是用户名不支持修改
	      /* beforeSubmit:function() {
	                  //检查用户是否已经被占用    1 username  2 phone  3 email
	                  $.ajax({
	                  type:"GET",
	                  dataType:"jsonp", // 跨服务器请求 必须用JSONP 
	                  url : UPDATE.param.surl + "/user/check/"+escape($("#username").val())+"/1?r=" + Math.random(),
	                  success : function(data) {
	                        if (data.data) {
	                         	UPDATE.doSubmit();
	                        } else {
	                              alert("此用户名已经被占用，请选择其他用户名");
	                              $("#username").select();
	                        }     
	                  }
	                  });
	                  
	      }, */
	      doSubmit:function() {
	      		$.ajax({
	                  type:"POST",
	                  dataType:"jsonp", /* 跨服务器请求 必须用JSONP */
	                  url : UPDATE.param.surl + "/user/update?r=" + Math.random(),
	                  data:$("#updateForm").serialize(),
	                  success : function(data) {
	                        if (data.status == 200) {
	                         	alert('用户信息更新成功！');
	                        } else {
	                              alert("更新失败！");
	                        }     
	                  }
	                  });
	      },
	      update:function() {
	            if (this.inputcheck()) {
	                 // this.beforeSubmit();
	                  this.doSubmit();
	            }
	      }
	};
	/* 更新用户信息结束 */
    </script>
    
</body>
</html>