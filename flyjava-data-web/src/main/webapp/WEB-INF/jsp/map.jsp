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
<title>测控研究室数据中心</title>
<meta name="description" content="东北农业大学测控研究室数据中心" />
<meta name="keywords"
	content="东北农业大学测控研究室数据中心，测控研究室，物联网 ，东北农业大学测控研究室， 农业物联网 ，农业物联网" />
<meta name="author" content="彭皖成" />

<!-- 适配 -->
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/font-awesome.css" rel="stylesheet">

<link href="/css/animate.css" rel="stylesheet">
<link href="/css/style.css" rel="stylesheet">
<!--百度地图-->
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=3.0&ak=pDnABGGS2xcdbVavMjmMkot12sRivQ8R"></script>

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
								<li><a href="<%=DATA_URL%>/user/loginOut">安全退出</a>
								</li>
							</ul>
						</div> <!-- 折叠侧边栏后显示的文字 -->
						<div class="logo-element">
							<h1>折叠侧边栏后显示的文字</h1>
						</div>
					</li>

					<li><a href="<%=DATA_URL%>/data/showUserHost">
							<i class="fa fa-bar-chart" aria-hidden="true"></i> <span
							class="nav-label"> 我的设备</span>
					</a></li>

					<li><a href="<%=DATA_URL%>/device-manager.html">
							<i class="fa fa-cogs" aria-hidden="true"></i> <span
							class="nav-label"> 设备管理</span>
					</a></li>

					<li class="active"><a
						href="<%=DATA_URL%>/map.html"> <i
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
							src="/images/logo_black.png" width="194" alt="测控研究室"></a> 数据中心
					</h2>

				</div>
			</div>



			<div class="row  border-bottom white-bg dashboard-header">
				<div class="col-sm-6">
					<h2>Map地图</h2>
				</div>
			</div>
			<!-- 百度地图 -->
			<div id="div_baidu_map" style="height: 600px;width:100%;"></div>
			<!--百度地图-->
			<script type="text/javascript">
			// 百度地图API功能
			/*GPS坐标
			var x = 126.7180746666666667;
			var y = 45.7487406666666667;
			var ggPoint = new BMap.Point(x, y);*/
			
				//前台js获得el表达式中的值，进行遍历json串
				var longitudeArray = new Array();
				var latitudeArray = new Array();
				<c:forEach items="${locationList}" var="list">  
				    longitudeArray.push(${list.longitude}); //js中可以使用此标签，将EL表达式中的值push到数组中  
					latitudeArray.push(${list.latitude}); 
				</c:forEach>
			
				//将经纬度数组中的 数据通过new BMap.Point(x,y) push到pointArr中
				var pointArr = new Array();
				for (var i = 0; i < longitudeArray.length; i++) {
					pointArr.push(new BMap.Point(longitudeArray[i], latitudeArray[i]));
				//alert(pointArr);
				}
			
			
			
				//获取el表达式中的产品id 和时间
				var productIdArray = new Array();
				var createdArray = new Array();
				<c:forEach items="${tbDataList}" var="list">  
				    productIdArray.push(${list.productId}); //js中可以使用此标签，将EL表达式中的值push到数组中  
				    createdArray.push("<fmt:formatDate value="${list.created}" pattern="yyyy/MM/dd hh:mm:ss"/>");
					//createdArray.push(new Date(${list.created}));  //<fmt:formatDate value="${list.created}" pattern="yyyy-MM-dd"/>
				</c:forEach>
				for (var i = 0; i < productIdArray.length; i++) {
					//alert(productIdArray[i]);
					//alert(createdArray[i].toString());
				}
				/* var pointArr = [
					new BMap.Point(126.7180746666666667, 45.7487406666666667),
					new BMap.Point(126.7180746666666667, 45.7486406666666667),
					new BMap.Point(126.7180746666666667, 45.7485406666666667),
					new BMap.Point(126.7180746666666667, 45.7484406666666667),
					new BMap.Point(126.7180746666666667, 45.7483406666666667)
				]; */
			
				//地图初始化            // 创建Map实例,设置地图允许的最小/大级别
				var bm = new BMap.Map("div_baidu_map", {
					minZoom : 4,
					maxZoom : 19
				});
				// 初始化地图,设置中心点坐标和地图级别  数字越大，越放大
				//bm.centerAndZoom(ggPoint, 15);
				//bm.centerAndZoom(pointArr[0], 16);
			
				var mapType1 = new BMap.MapTypeControl({
					mapTypes : [ BMAP_NORMAL_MAP, BMAP_HYBRID_MAP ]
				});
				var mapType2 = new BMap.MapTypeControl({
					anchor : BMAP_ANCHOR_TOP_RIGHT
				});
				var mapType3 = new BMap.NavigationControl({
					anchor : BMAP_ANCHOR_TOP_LEFT,
					type : BMAP_NAVIGATION_CONTROL_SMALL
				});
				//添加地图类型控件
				bm.addControl(mapType1); //2D图，卫星图
				bm.addControl(mapType2); //左上角，默认地图控件
				bm.addControl(mapType3); //右上角，仅包含平移和缩放按钮
				//bm.setCurrentCity("哈尔滨");        //由于有3D图，需要设置城市哦
			
			
			
				//坐标转换完之后的回调函数
				var count = 0;
				var infst = "";
				translateCallback = function(data) {
					if (data.status === 0) {
						//初始化参数 
						var gps_x = data.points[0].lng;
						var gps_x = gps_x.toFixed(6); //取小数点后面5位
			
						var gps_y = data.points[0].lat;
						var gps_y = gps_y.toFixed(6); //取小数点后面5位
						
						//关于信息窗口的展示对象初始化
						var date=createdArray[count];  //时间
						var soidtem1=16.2535;         //土壤5cm 温度
						var soidtem2=15.1845;         //土壤10cm 温度
						var soidtem3=12.5864;         //土壤15cm 温度
						var soidhum=20;              //土壤湿度10cm 
						var soidph=0;               //土壤PH
						var airtem=20;              //空气温度
						var airhum=30;               //空气湿度
						var winddirection="西北";    //风向
						var windspeed=0;           //风速
						var light=0;               //光照强度
						var rain=0;                //降雨量
						var voltage=5.2;            //电压
			
						// 创建标注
						var marker = new BMap.Marker(data.points[0]);
						// 将标注添加到地图中
						bm.addOverlay(marker);
						var productID = productIdArray[count];
						//alert(productID);
						var infst ="";// "ID：" + productID + " 坐标：" + gps_x + " , " + gps_y;
			
						var opts = {
							//position: data.points[0], // 指定文本标注所在的地理位置
							offset : new BMap.Size(-20, -20) //设置文本偏移量  左右（正右） 上下 （正下）
						}
			
						var label = new BMap.Label(infst, opts);
						label.setStyle({
							backgroundColor : "0.05", //文本标注背景颜色　
							color : "blue", //字体颜色
							fontSize : "12px", //字体大小
							border : "0",
							//height: "10px",
							//lineHeight: "10px",
							fontFamily : "微软雅黑"
						});
						marker.setLabel(label); //添加百度label
						//bm.setCenter(data.points[0]);
						count++;
			
						
						
						
						//信息窗口对象  开始
						var _opts = {
							title : "采集节点ID:"+ productID
						};
						var _content = "<div style='font-family:微软雅黑;font-size:9px; text-align:center;  height:auto;width:285px;border:1px solid green;padding:0px;margin:0px;'>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>经&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;纬&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度：</b><b style='display:block;float:right;width:160px;border-bottom:1px solid silver;'>" + gps_x + "&nbsp;,&nbsp;" + gps_y + " </b>  </div>  <div style='clear:both;'></div> "
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>时&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ date + "</b></div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>土壤温度 5cm(℃)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'> "+ soidtem1+"</b>   </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>土壤温度10cm(℃)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ soidtem2+ "</b>  </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>土壤温度15cm(℃)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ soidtem3  + "</b>     </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>土壤湿度10cm(%)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ soidhum  + "</b>       </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>土&nbsp;&nbsp;&nbsp;&nbsp;壤&nbsp;&nbsp;&nbsp;&nbsp;P&nbsp;&nbsp;&nbsp;&nbsp;H&nbsp;&nbsp;&nbsp;：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ soidph  + "</b>   </div>  <div style='clear:both;'></div>"			   
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>空&nbsp;&nbsp;气&nbsp;&nbsp;温&nbsp;&nbsp;度&nbsp;&nbsp;(℃)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ airtem  + "</b>     </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>空&nbsp;&nbsp;气&nbsp;&nbsp;湿&nbsp;&nbsp;度&nbsp;&nbsp;(%)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ airhum  + "</b>      </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>风&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;向：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ winddirection  + "</b>   </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>风&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;速&nbsp;&nbsp;(m/s)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ windspeed  + "</b>    </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>光&nbsp;&nbsp;&nbsp;&nbsp;照&nbsp;&nbsp;&nbsp;&nbsp;强&nbsp;&nbsp;&nbsp;&nbsp;度：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ light  + "</b>   </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>降&nbsp;&nbsp;&nbsp;&nbsp;雨&nbsp;&nbsp;&nbsp;量(mm)：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ rain  + "</b>   </div>  <div style='clear:both;'></div>"
			   +"<div style='margin-top:1px; '> <b style='diaplsy:block;float:left; width:120px;'>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;压：</b><b style='display:block;float:right; width:160px;border-bottom:1px solid silver;'>"+ voltage  + "</b>   </div>  <div style='clear:both;'></div>"
			   +"</div>";
			
						var infoWindow = new BMap.InfoWindow(_content, _opts); // 创建信息窗口对象
						//点击时候弹出
						marker.addEventListener("click", function() {
							this.openInfoWindow(infoWindow);
							//js 异步请求 把剩下的参数填充
							//根据产品ID AJAX异步请求 获取气象数据  开始
								$.ajax({
									url : 'http://data.ck.neau.edu.cn/data/showWeather',
									type : 'POST',
									data : {
										"productId" : productId
									},
									async : false, //或false,是否异步
									datatype : "json",
									success : function(data) {
										//将数据赋值到maker 标记中 开始
					
										//将数据赋值到maker 标记中 结束
									},
									error : function(XMLHttpRequest, txtStatus, errorThrown) { //alert(XMLHttpRequest + "<br>" + txtStatus + "<br>" + errorThrown);
									}
								});
							//根据产品ID AJAX异步请求 获取气象数据  结束
			
						});
						//信息窗口对象  结束
			
			
			
			
						
					}
				}
			
				//定时任务  1000毫秒后执行
			
				for (let i = 0; i < pointArr.length; i++) {
					setTimeout(function() {
						var convertor = new BMap.Convertor();
						var pointArr_covert = [];
			
						pointArr_covert.push(pointArr[i]);
						convertor.translate(pointArr_covert, 1, 5, translateCallback)
						bm.centerAndZoom(pointArr_covert[0], 15);
					//console.log(i);
					}, 1000 * i);
				}
			</script>
		</div>
		<!-- 内容区域end -->


	</div>

	<!-- Mainly scripts -->
	<script src="/js/jquery-2.1.1.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="/js/hplus.js"></script>
	<script src="/js/plugins/pace/pace.min.js"></script>


</body>
</html>