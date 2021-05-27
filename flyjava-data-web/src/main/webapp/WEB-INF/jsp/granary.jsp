<%@ page language="java" import="java.util.*,java.io.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<title>测控研究室数据中心--粮仓监测</title>
<meta name="description" content="东北农业大学测控研究室数据中心" />
<meta name="keywords"
	content="东北农业大学测控研究室数据中心，测控研究室，物联网 ，粮仓监测，东北农业大学测控研究室， 农业物联网 ，农业物联网" />
<meta name="author" content="彭皖成" />

<!-- 适配 -->
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/font-awesome.css" rel="stylesheet">

<!-- Morris -->
<!-- <link href="/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">-->
<!-- Gritter -->
<!-- <link href="/js/plugins/gritter/jquery.gritter.css" rel="stylesheet"> -->

<link href="/css/animate.css" rel="stylesheet">
<link href="/css/style.css" rel="stylesheet">
<!--  日期插件-->
<link href="/css/bootstrap-datetimepicker.min.css" rel="stylesheet">

<!--表格控件css  -->
<link href="/css/jquery.dataTables.min.css" rel="stylesheet">

</head>

<body class="fixed-sidebar">
	<div id="wrapper">
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav" id="side-menu">
					<li class="nav-header">
						<div class="dropdown profile-element">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#"> <span
								class="clear"> <span class="block m-t-xs"> <strong
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

					<li><a href="<%=DATA_URL%>/data/showUserHost">
							<i class="fa fa-bar-chart" aria-hidden="true"></i> <span
							class="nav-label"> 我的设备 </span>
					</a></li>

					<li><a href="<%=DATA_URL%>/device-manager.html">
							<i class="fa fa-cogs" aria-hidden="true"></i> <span
							class="nav-label"> 设备管理</span>
					</a></li>

					<li><a href="<%=DATA_URL%>/map.html"> <i
							class="fa fa-location-arrow" aria-hidden="true"></i> <span
							class="nav-label"> 设备分布</span>
					</a></li>

					<li class="active"><a
						href="<%=DATA_URL%>/granary?productId="> <i
							class="fa fa-location-arrow" aria-hidden="true"></i> <span
							class="nav-label"> 粮仓监测</span>
					</a></li>

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
							src="/images/logo_black.png" width="194" alt="测控研究室"></a> 粮仓监测
					</h2>

				</div>
			</div>


			<!-- 温度  图表区域 -->
			<div class="row  border-bottom white-bg dashboard-header">

				<!--日期搜索  -->
				<from class="form-inline" style="margin-left:5px;">
				<div class="form-group">
					<div class="input-group form_datetime">
						<span class="input-group-addon">开始时间</span> <input size="10"
							type="text" id="datetimeStart" readonly class="form-control"
							autocomplete="off"> <span class="input-group-addon"><span
							class="glyphicon glyphicon-remove remove-start"></span></span>
					</div>
					<div class="input-group form_datetime">
						<span class="input-group-addon">结束时间</span> <input size="10"
							type="text" id="datetimeEnd" readonly class="form-control"
							autocomplete="off"> <span class="input-group-addon"><span
							class="glyphicon glyphicon-remove remove-end"></span></span>
					</div>
				</div>

				<button type="button" id="searchButton" class="btn btn-primary"
					onclick="SEARCH.search();">温湿度查询</button>
				</from>
				<!--日期搜索  结束 -->
				<div class="hr-line-dashed"></div>


				<div class="col-sm-12">
					<h2 class="text-info text-center">粮仓温度显示</h5>
					<!-- 图表占位容器 -->
					<div id="temperatureCharts" style="width:100%;height:200px;"></div>
				</div>

			</div>
			<!--温度  图表区域   结束-->

			<!-- 粮仓湿度  区域 -->
			<div class="row  border-bottom white-bg dashboard-header">
				<div class="col-sm-12">
					<h2 class="text-info text-center">粮仓湿度显示</h5>
					<!-- 图表占位容器 -->
					<div id="humidityCharts" style="width:100%;height:200px;"></div>
				</div>

			</div>
			<!--粮仓湿度   结束-->
			
			<!-- 表格 开始 -->
			<h2 class="text-warning text-center">粮仓最新温湿度表格显示</h5>
			<table id="example" class="table table-striped table-bordered table-hover" >
				<THEAD>
					<tr class="success">
						<td>序号</td>
						<td>0</td>
						<td>1</td>
						<td>2</td>
						<td>3</td>
						<td>4</td>
						<td>5</td>
						<td>6</td>
						<td>7</td>
						<td>8</td>
						<td>9</td>
						<td>10</td>
						<td>11</td>
						<td>12</td>
						<td>13</td>
						<td>14</td>
						<td>15</td>
						<td>16</td>
						<td>17</td>
						<td>18</td>
						<td>19</td>
					</tr>
				</THEAD>
				<tbody>
				</tbody>
			</table>

		</div>
		<!-- 内容区域end -->


	</div>

	<!-- Mainly scripts -->
	<script src="/js/jquery-2.1.1.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Flot 
    <script src="/js/plugins/flot/jquery.flot.js" ></script>
    <script src="/js/plugins/flot/jquery.flot.tooltip.min.js" ></script>
    <script src="/js/plugins/flot/jquery.flot.spline.js" ></script>
    <script src="/js/plugins/flot/jquery.flot.resize.js" ></script>
    <script src="/js/plugins/flot/jquery.flot.pie.js" ></script>-->

	<!-- Peity 
    <script src="/js/plugins/peity/jquery.peity.min.js" ></script>
    <script src="/js/demo/peity-demo.js" ></script>-->

	<!-- Custom and plugin javascript -->
	<script src="/js/hplus.js"></script>
	<script src="/js/plugins/pace/pace.min.js"></script>

	<!-- jQuery UI -->
	<!--  <script src="/js/plugins/jquery-ui/jquery-ui.min.js" ></script> -->

	<!-- GITTER -->
	<!-- <script src="/js/plugins/gritter/jquery.gritter.min.js" ></script> -->

	<!-- EayPIE -->
	<!--  <script src="/js/plugins/easypiechart/jquery.easypiechart.js" ></script> -->

	<!-- Sparkline -->
	<!-- <script src="/js/plugins/sparkline/jquery.sparkline.min.js" ></script> -->

	<!-- Sparkline demo data  -->
	<!--  <script src="/js/demo/sparkline-demo.js" ></script> -->

	<!-- Echarts -->
	<script src="/js/echarts/echarts.min.js"></script>
	<!-- Echarts  theme
    <script src="/js/echarts/theme/macarons.js" ></script>-->
    
    <!-- 表格控件 -->
    <script src="/js/jquery.dataTables.min.js" ></script>-->

	<!--日期插件 默认英文 所以加了中文包 -->
	<script src="/js/bootstrap-datetimepicker.js"></script>
	<script src="/js/bootstrap-datetimepicker.zh-CN.js"></script>

	<!-- 日期插件
    	 startDate:new Date("2017,1,1")
    	  endDate:new Date() 
     -->

	<!-- 粮仓温度查询输入检查 -->
	<script type="text/javascript">
		/* 点击选择时间后面的X 就清空时间 */
		$(".remove-start").click(function() {
			$("#datetimeStart").val("");
		});
	
		$(".remove-end").click(function() {
			$("#datetimeEnd").val("");
		});
	
		$("#datetimeStart").datetimepicker({
			format : 'yyyy-mm-dd', //显示格式
			minView : 'month', //设置只显示到月份
			language : 'zh-CN', //显示中文
			autoclose : true, //选中自动关闭
		}).on("click", function() {
			if ($("#datetimeEnd").val() == "") {
				$("#datetimeStart").datetimepicker("setEndDate", new Date()); //开始时间
			} else {
				$("#datetimeStart").datetimepicker("setEndDate", $("#datetimeEnd").val())
			}
		});
		$("#datetimeEnd").datetimepicker({
			format : 'yyyy-mm-dd',
			minView : 'month',
			language : 'zh-CN',
			autoclose : true,
			endDate : new Date()
		}).on("click", function() {
			if ($("#datetimeStart").val() != "") {
				//设置开始时间为 左边的值
				$("#datetimeEnd").datetimepicker("setStartDate", $("#datetimeStart").val())
			}
	
		});
	</script>
	<!-- 粮仓温度查询输入检查 结束 -->

	<!-- 粮仓温度 初始化 折线图显示 -->
	<script type="text/javascript">
		//页面初始化加载图
		$(function() {
			/* 页面第一次加载完成 初始图表,使其显示  */
			reloadTemperatureChart();
		});
	
		/*  接受设备列表 传入本页的 产品ID,用于与后台数据交互使用 */
		var productId = "${productId}";
	
		// 基于准备好的dom，初始化echarts实例
		var temperatureChart = echarts.init(document.getElementById('temperatureCharts'));
		// 指定图表的配置项和数据
		var temperatureOption = {
			title : {
				text : '' /*粮仓温度数据折线图 */
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'cross',
					label : {
						backgroundColor : '#6a7985'
					}
				}
			},
			legend : {
				data : [ 'T0', 'T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12', 'T13', 'T14', 'T15', 'T16', 'T17', 'T18', 'T19' ]
			},
			toolbox : {
				show : true,
				feature : {
					dataZoom : {
						yAxisIndex : 'none'
					},
					dataView : {
						readOnly : false
					},
					magicType : {
						type : [ 'line', 'bar' ]
					},
					restore : {},
					saveAsImage : {}
				}
			},
			grid : {
				top : '12%',
				left : '3%',
				right : '8%',
				bottom : '10%',
				containLabel : true
			},
			xAxis : [
				{
					type : 'category',
					boundaryGap : false,
					data : []
				}
			],
			yAxis : [
				{
					type : 'value',
					axisLabel : {
						formatter : '{value} °C'
					}
				}
			],
			series : []
		};
		/* 使用刚指定的配置项和数据显示图表。 */
		temperatureChart.setOption(temperatureOption);
	
		/* echarts图响应式变化 */
		window.addEventListener('resize', function() {
			temperatureChart.resize();
		});
	
		/*异步刷新折线图*/
		var temperatureTimer= setInterval('reloadTemperatureChart()',30000); //每30秒刷新一次页面下边显示的数据
		
		temperatureChart.showLoading(); //数据加载完之前先显示一段简单的loading动画
		function reloadTemperatureChart() {
			$.ajax({
				url : '<%=DATA_URL%>/data/showGranaryTemperature',
				type : 'POST',
				data : {
					"productId" : productId
				},
				async : false, //或false,是否异步
				datatype : "json",
				success : function(data) {
					temperatureChart.hideLoading(); //隐藏加载动画
					// 填入数据
					temperatureChart.setOption({
						xAxis : {
							data : data.categories //X轴 插入数据
						},
						series : data.data //Y轴插入多条记录
					});
				},
				error : function(XMLHttpRequest, txtStatus, errorThrown) { //alert(XMLHttpRequest + "<br>" + txtStatus + "<br>" + errorThrown);
				}
			});
		}
	</script>
	<!-- 粮仓 温度  结束 -->

	<!-- 粮仓 shidu湿度 初始化 折线图显示   humidityCharts-->
	<script type="text/javascript">
		//页面初始化加载图
		$(function() {
			/* 页面第一次加载完成 初始图表,使其显示  */
			reloadHumidityChart();
		});
		// 基于准备好的dom，初始化echarts实例
		var humidityChart = echarts.init(document.getElementById('humidityCharts'));
		// 指定图表的配置项和数据
		var humidityOption = {
			title : {
				text : '' /*粮仓湿度数据折线图 */
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'cross',
					label : {
						backgroundColor : '#6a7985'
					}
				}
			},
			legend : {
				data : [ 'H0', 'H1', 'H2', 'H3', 'H4', 'H5', 'H6', 'H7', 'H8', 'H9', 'H10' , 'H11', 'H12', 'H13', 'H14', 'H15', 'H16', 'H17', 'H18', 'H19']
			},
			toolbox : {
				show : true,
				feature : {
					dataZoom : {
						yAxisIndex : 'none'
					},
					dataView : {
						readOnly : false
					},
					magicType : {
						type : [ 'line', 'bar' ]
					},
					restore : {},
					saveAsImage : {}
				}
			},
			grid : {
				top : '12%',
				left : '3%',
				right : '8%',
				bottom : '10%',
				containLabel : true
			},
			xAxis : [
				{
					type : 'category',
					boundaryGap : false,
					data : []
				}
			],
			yAxis : [
				{
					type : 'value',
					axisLabel : {
						formatter : '{value} %RH'
					}
				}
			],
			series : []
		};
		/* 使用刚指定的配置项和数据显示图表。 */
		humidityChart.setOption(humidityOption);
	
		/* echarts图响应式变化 */
		window.addEventListener('resize', function() {
			humidityChart.resize();
		});
	
		/*异步刷新折线图*/
	    var humidityTimer= setInterval('reloadHumidityChart()',30000); //每30秒刷新一次页面下边显示的数据
		humidityChart.showLoading();    //数据加载完之前先显示一段简单的loading动画
		function reloadHumidityChart() {
			$.ajax({
				url : '<%=DATA_URL%>/data/showGranaryHumidity',
				type : 'POST',
				data : {
					"productId" : productId
				},
				async : false, //或false,是否异步
				datatype : "json",
				success : function(data) {
					humidityChart.hideLoading();    //隐藏加载动画
					// 填入数据
					humidityChart.setOption({
						xAxis : {
							data : data.categories //X轴 插入数据
						},
						series : data.data //Y轴插入多条记录
					});
				},
				error : function(XMLHttpRequest, txtStatus, errorThrown) { //alert(XMLHttpRequest + "<br>" + txtStatus + "<br>" + errorThrown);
				}
			});
		}
	</script>
	<!-- 粮仓 湿度  结束 -->

	<!-- 粮仓温度  按照时间查询数据
		1 ,停止定时器
		2, 异步请求
	 -->
	<script>
		var SEARCH = {
			param : {
				//数据系统的url
				surl : "<%=DATA_URL%>"+""
			},
			inputcheck : function() {
				//不能为空检查
				if ($("#datetimeStart").val() == "") {
					alert("查询时间不能为空");
					$("#datetimeStart").focus();
					return false;
				}
				if ($("#datetimeEnd").val() == "") {
					alert("查询时间不能为空");
					$("#datetimeEnd").focus();
					return false;
				}
				return true;
			},
			doSubmit : function() {
				//停止定时器
				clearInterval(temperatureTimer);
				clearInterval(humidityTimer);
	
				//获取参数
				var start = $("#datetimeStart").val();
				var end = $("#datetimeEnd").val();
				$.ajax({
					type : "POST",
					url : SEARCH.param.surl + "/data/searchGranary?r=" + Math.random(),
					data : {
						"productId" : productId,
						"start" : start,
						"end" : end
					},
					async : false, //或false,是否异步
					datatype : "json",
					success : function(data) {
					
						//温度Temp
						temperatureChart.hideLoading(); //隐藏加载动画
						// 填入数据
						temperatureChart.setOption({
							/* 设置 可以拖动的滑动块 */
							dataZoom : [
								{
									//滑块
									type : 'slider',
									show : true,
									xAxisIndex : [ 0 ],
									start : 1,
									end : 5
								},
								{
									//滑块
									type : 'slider',
									show : true,
									yAxisIndex : [ 0 ],
									left : '93%',
									/* 滑块的宽度 */
									start : 0,
									end : 100
								},
								{
									type : 'inside',
									xAxisIndex : [ 0 ],
									start : 1,
									end : 99
								},
								{
									type : 'inside',
									yAxisIndex : [ 0 ],
									start : 1,
									end : 99
								}
							],
							xAxis : {
								data : data.categories //X轴 插入数据
							},
							series : data.temperatureData //Y轴插入多条记录
						});
						
						
						//湿度
						humidityChart.hideLoading(); //隐藏加载动画
						// 填入数据
						humidityChart.setOption({
							/* 设置 可以拖动的滑动块 */
							dataZoom : [
								{
									//滑块
									type : 'slider',
									show : true,
									xAxisIndex : [ 0 ],
									start : 1,
									end : 5
								},
								{
									//滑块
									type : 'slider',
									show : true,
									yAxisIndex : [ 0 ],
									left : '93%',
									/* 滑块的宽度 */
									start : 0,
									end : 100
								},
								{
									type : 'inside',
									xAxisIndex : [ 0 ],
									start : 1,
									end : 99
								},
								{
									type : 'inside',
									yAxisIndex : [ 0 ],
									start : 1,
									end : 99
								}
							],
							xAxis : {
								data : data.categories //X轴 插入数据
							},
							series : data.humidityData //Y轴插入多条记录
						});
					},
					error : function(XMLHttpRequest, txtStatus, errorThrown) { //alert(XMLHttpRequest + "<br>" + txtStatus + "<br>" + errorThrown);
					}
				});
			},
			search : function() {
				//只有通过了非空检测 和产品ID重复检测才能提交
				if (this.inputcheck()) {
					this.doSubmit();
				}
			}
		};
	</script>
	<!-- 粮仓温度 查询结束 -->

	<!-- 表格控件 -->
	<script>
	//页面初始化加载图
	$(function() {
		/* 页面第一次加载完成 初始表,使其显示  */
		DataTableInit();
	});
	
	var tables;
	//初始化dataTables  
    function DataTableInit() {
         tables=$('#example').DataTable({
	            "bPaginate": false, //翻页功能
				"bLengthChange": false, //改变每页显示数据数量
				"bFilter": false, //过滤功能
				"bSort": false, //排序功能
				"bInfo": false,//页脚信息
				"bAutoWidth": false,//自动宽度
	            ajax: {
		        		type : "POST",
						url : "<%=DATA_URL%>"+"/data/showGranaryTables",
						data : {
							"productId" : productId,
						},
						async : false, //或false,是否异步
						datatype : "json",
			    },
			    //每页显示2条数据
			    pageLength: 2,
			    columns: [ 
			    	{"data": "title"}, //显示 标题
				    {"data": "t0"},
				    {"data": "t1"},
				    {"data": "t2"},
				    {"data": "t3"},
				    {"data": "t4"},
				    {"data": "t5"},
				    {"data": "t6"},
				    {"data": "t7"},
				    {"data": "t8"},
				    {"data": "t9"},
				    {"data": "t10"},
				    {"data": "t11"},
				    {"data": "t12"},
				    {"data": "t13"},
				    {"data": "t14"},
				    {"data": "t15"},
				    {"data": "t16"},
				    {"data": "t17"},
				    {"data": "t18"},
				    {"data": "t19"}
			    ],
			     "columnDefs": [{
		                "searchable": false,
		                "orderable": false,
		                "targets": 0
		        }]
	        });
    }
	//定时刷新   //http://www.datatables.club/reference/api/ajax.reload().html
	setInterval( function () {
	    tables.ajax.reload(null, false ); // 刷新表格数据，分页信息不会重置
	}, 30000 );
    
</script>
</body>
</html>