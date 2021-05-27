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

					<li class="active"><a href="<%=DATA_URL%>/device-manager.html">
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

					<li><a href="<%=DATA_URL%>/info.html"> <i
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
							src="/images/logo_black.png" width="194" alt="测控研究室"></a>
						数据中心
					</h2>

				</div>
			</div>



			<div class="row  border-bottom white-bg dashboard-header">
				<div class="col-sm-6">
					<h2>设备管理</h2>
					<hr>
					<h3>
						第一步:添加主机 <small class="text-warning">提示: 一个主机可以添加多个传感设备,添加过主机可以进行第二步 !!!</small>
					</h3>
					<br>
					<!--添加主机  -->
					<div class="form-group">
						<label class="col-sm-2 control-label">主机名称 </label>
						<div class="col-sm-8">
							<input type="text" id="hostNameInput" name="hostNameInput" maxlength="20"
								class="form-control" placeholder="请输入合适的主机名称" value=""
								tabindex="1" autocomplete="off onblur="SAVEHOST.check();">
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn btn-primary"
								onclick="SAVEHOST.update();">确定添加</button>
						</div>
					</div>

					<div class="clearfix"></div>
					<br>
					<div class="hr-line-dashed"></div>
					<h3>第二步:添加设备</h3>
					<br>
					<!--表单开始-->
					<form id="bindingForm" class="form-horizontal" method="post"
						onsubmit="return false;">

						<div class="input-group">
							<span class="input-group-addon">
								<button type="button" class="btn btn-primary btn-xs dropdown-toggle"
									data-toggle="dropdown" onclick="SHOWHOSTNAME.show();">
									主机名称 <span class="caret"></span>
								</button>
								
								<ul id="hostNameSelect" class="dropdown-menu list-group"">
									<!--  动态添加Li-->
								</ul>
							</span>
							<input  id="hostName" type="text" class="form-control" placeholder="请选择主机名称" disabled="disabled"/>
							<!-- 存放主机Id -->
							<input name="hostId" id="hostId" type="text" class="form-control hidden"/>
						</div>

						<br>
						

						<div class="input-group">
							<span class="input-group-addon">
								<button type="button" class="btn btn-primary btn-xs dropdown-toggle"
									data-toggle="dropdown" onclick="SHOWPRODUCTCAT.show();">
									设备名称 <span class="caret"></span>
								</button>
								
								<ul id="productCatSelect" class="dropdown-menu list-group"">
									<!--  动态添加Li-->
									<!-- <li>11</li> -->
								</ul>
							</span>
							<input  id="productCatName" type="text" class="form-control" placeholder="请选择设备种类" disabled="disabled"/>
							<!-- 存放productCatId -->
							<input name="productCatId" id="productCatId" type="text" class="form-control hidden"/>
						</div>
						<div class="hr-line-dashed"></div>

						<!-- 产品ID -->
						<div class="form-group">
							<label class="col-sm-2 control-label">产品ID </label>
							<div class="col-sm-10">
								<input type="number" style="ime-mode:disabled;" id="productId"
									name="productId" maxlength="20" class="form-control"
									placeholder="" style="ime-mode:disabled;" value="" tabindex="1" autocomplete="off">
							</div>
						</div>
						<div class="hr-line-dashed"></div>


						<div class="form-group">
							<div class="col-sm-12">
								<button class="btn btn-primary center-block" type="submit" id="updatesubmit"
									tabindex="4" onclick="BINDING.update();">确认绑定</button>
							</div>
						</div>

					</form>
					<!--表单结束-->
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

	<script>
		//数据系统的url
		var surl = "<%=DATA_URL%>"+""
		/*保存主机名称*/
		var SAVEHOST = {
			param : {
				//数据系统的url
				surl : "<%=DATA_URL%>"+""
			},
			inputcheck : function() {
				//不能为空检查
				if ($("#hostNameInput").val() == "") {
					alert("主机名称不能为空");
					$("#hostNameInput").focus();
					return false;
				}
	
				return true;
			},
			checkHostName:function(){
				//检查主机名称是否重复
				var $hostName = $("#hostNameInput").val();
				//ajax
				if ($hostName != "") {
					$.ajax({
						type : "POST",
						url : surl + "/check/hostName?r=" + Math.random(),
						data : {
							"hostName" : $hostName
						},
						success : function(data) {
						    if (data.status == 200) {
						       alert('主机名称可以使用');
						        return true;
						    }
							if (data.status == 400) {
								$("#hostNameInput").val("");
								$("#hostNameInput").focus();
								alert('主机名称重复！');
								return false;
							}
						}
					});
				}
				return true;
			},
			doSubmit : function() {
				var $hostName = $("#hostNameInput").val();
				$.ajax({
					type : "POST",
					url : surl+ "/data/saveHost?r=" + Math.random(),
					data : {
						"hostName" : $hostName
					},
					success : function(data) {
						if (data.status == 200) {
							alert('新建主机成功！');
						} else {
							alert("新建主机失败！");
						}
					}
				});
			},
			update : function() {
				//先非空判断
				if (this.inputcheck()) {
					//在检查是否已经存在
					//alert(this.checkHostName());
					if(this.checkHostName()){
						this.doSubmit();
					}
				}
			}
		};
		/* 保存主机信息结束 */
	
	
		
		/*AjAX请求服务器  动态显示用户的主机名称*/
		var SHOWHOSTNAME={
		     show:function(){
		     	//清空ul里面的内容
		     	$("#hostNameSelect").html("");
		       $.ajax({
					type : "POST",
					url : surl + "/showHostName",
					success : function(data) {
						jQuery.each(data.data, function(i,item){
						    $("#hostNameSelect").append("<li class='list-group-item' value="+item.hostId+">"+item.hostName+"</li>");
					        //alert(item.id+","+item.hostName);  
					  	});  

					}
				});
		     }
		}
		
		/* 选择主机名称 通过点击Ul 同步到输入框 */
		$("#hostNameSelect").on("click", "li", function(){
		        $("#hostName").val($(this).text());
		        //将主机id放在 主机名称下面隐藏的input中
		        $("#hostId").val($(this).attr("value")); 
		});
		
		
		/*AjAX请求服务器  动态显示传感器种类*/
		var SHOWPRODUCTCAT={
		     show:function(){
		     	//清空ul里面的内容
		     	$("#productCatSelect").html("");
		       $.ajax({
					type : "POST",
					url : surl + "/showProductCat",
					success : function(data) {
						jQuery.each(data.data, function(i,item){
						    $("#productCatSelect").append("<li class='list-group-item' value="+item.productCatId+">"+item.name+"</li>");
					        //alert(item.id+","+item.hostName);  
					  	});  

					}
				});
		     }
		}
		/* 选择设备名称   通过点击Ul 同步到输入框 */
		$("#productCatSelect").on("click", "li", function(){
		        $("#productCatName").val($(this).text());
		        //将主机id放在 主机名称下面隐藏的input中
		        $("#productCatId").val($(this).attr("value")); 
		});
		
	
		/*绑定设备到主机*/
		var BINDING = {
			param : {
				//数据系统的url
				surl : "<%=DATA_URL%>"+""
			},
			inputcheck : function() {
				//不能为空检查
				if ($("#hostName").val() == "") {
					alert("主机名称不能为空");
					$("#hostName").focus();
					return false;
				}
				if ($("#productCatName").val() == "") {
					alert("设备类别名称不能为空");
					$("#productCatName").focus();
					return false;
				}
				if ($("#productId").val() == "") {
					alert("产品ID不能为空");
					$("#productId").focus();
					return false;
				}
				return true;
			},
			checkProduct:function(){
				//检查产品ID,是否重复
				var $productId = $("#productId").val();
				//ajax
				if ($productId != "") {
					$.ajax({
						type : "POST",
						url : surl + "/check/productId",
						data : {
							"productId" : $productId
						},
						success : function(data) {
							if (data.status == 400) {
								alert('productId名称重复！');
								$("#productId").val("");
								$("#productId").focus();
								return false;
							}
							return true;						}
					});
		
				}
				return true;
			},
			doSubmit : function() {
				$.ajax({
					type : "POST",
					url : BINDING.param.surl + "/bindProduct?r=" + Math.random(),
					data : $("#bindingForm").serialize(),
					success : function(data) {
						if (data.status == 200) {
							alert('设备绑定成功！');
						} else {
							alert("设备绑定失败！");
						}
					}
				});
			},
			update : function() {
				//只有通过了非空检测 和产品ID重复检测才能提交
				if (this.inputcheck()) {
					if(this.checkProduct()){
						this.doSubmit();
					}
				}
			}
		};
		/* 绑定设备信息结束 */
	</script>
</body>
</html>