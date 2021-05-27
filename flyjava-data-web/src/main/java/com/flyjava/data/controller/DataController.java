package com.flyjava.data.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flyjava.data.pojo.EchartGranaryResult;
import com.flyjava.data.pojo.EchartSearchGranaryResult;
import com.flyjava.data.pojo.EchartTempResult;
import com.flyjava.data.pojo.Granary;
import com.flyjava.data.pojo.Granary20;
import com.flyjava.data.pojo.GranaryTables;
import com.flyjava.data.pojo.HostListResult;
import com.flyjava.data.pojo.Location;
import com.flyjava.data.pojo.ProductListResult;
import com.flyjava.data.pojo.TbData;
import com.flyjava.data.pojo.Temp;
import com.flyjava.data.service.DataService;
import com.flyjava.sso.pojo.TbUser;
import com.flyjava.utils.FlyjavaResult;
import com.flyjava.utils.JsonUtils;

/**
 * 关于数据的
 * 	数据接受
 * 	展示数据json
 * 	展示用户所有的设备,支持分页
 * 	展示数据页面  有图表 (图表数据是进行ajax异步请求得到并展示的)
 * @author 彭同学
 *
 */
@Controller
public class DataController {
	//温度上限
	@Value("${TEMP_UPPER_LIMIT}")
	private float TEMP_UPPER_LIMIT;
	//温度下限
	@Value("${TEMP_LOWER_LIMIT}")
	private float TEMP_LOWER_LIMIT;
	
	//拦截通过,存放request域中user对象的名称
	@Value("${LOGIN_USER}")
	private String LOGIN_USER;
	
	//展示设备列表 主机列表  一页显示设备的条数
	@Value("${DEVICE_ROWS}")
	private int DEVICE_ROWS;
	
	//保存土壤温度传感器类别ID
	@Value("${TEMP_PRODUCT_CAT_ID}")
	private int TEMP_PRODUCT_CAT_ID;
	//保存实时土壤温度数据到redis的key
	@Value("${REDIS_KEY_TEMP}")
	private String REDIS_KEY_TEMP;
	//保存在redis temp数据list的总长度
	@Value("${REDIS_LENGTH_TEMP}")
	private Long REDIS_LENGTH_TEMP;
	
	//保存GPS传感器类别ID
	@Value("${GPS_PRODUCT_CAT_ID}")
	private int GPS_PRODUCT_CAT_ID;
	//保存GPS数据到redis的key
	@Value("${REDIS_KEY_GPS}")
	private String REDIS_KEY_GPS;
	//保存在redis GPS数据list的总长度
	@Value("${REDIS_LENGTH_GPS}")
	private Long REDIS_LENGTH_GPS;
	
	//保存粮仓温湿度数据到redis的key
	@Value("${REDIS_KEY_GRANARY}")
	private String REDIS_KEY_GRANARY;
	//保存在redis 粮仓温湿度 list的总长度
	@Value("${REDIS_LENGTH_GRANARY}")
	private Long REDIS_LENGTH_GRANARY;
	
	@Autowired
	private DataService dataService;

	/**
	 * 根据用户id 查询用户所有的主机,支持分页
	 * 返回jsp 页面 
	 * @return
	 */
	@RequestMapping("/data/showUserHost")
	public String  showHostListByUserId(@RequestParam(defaultValue="1")int page,
			Model model,HttpServletRequest request)throws Exception{
		//从request中获取user对象
		TbUser user = (TbUser) request.getAttribute(LOGIN_USER);
		//获取userID
		Long userId = user.getId();
		
		//调用服务 得到结果
		HostListResult result = dataService.getHostListByUserId(userId, page, DEVICE_ROWS);
		//返回数据
		model.addAttribute("hostList",result.getHostList());
		model.addAttribute("page",page);
		model.addAttribute("totalPages",result.getPageCount());
		//页面展现用户名
		model.addAttribute("user",user);
		
		return "host";
	}
	
	/**
	 * 根据用户id 查询一个主机对应的所有的产品,支持分页
	 * 返回jsp 页面 
	 * @return
	 */
	@RequestMapping("/data/showUserProduct")
	public String  showHostListByUserId(@RequestParam(defaultValue="1")int page,@RequestParam Long hostId,
			Model model,HttpServletRequest request)throws Exception{
		//从request中获取user对象
		TbUser user = (TbUser) request.getAttribute(LOGIN_USER);
		
		
		//调用服务 得到结果
		ProductListResult result = dataService.getProductListByHostId(hostId, page, DEVICE_ROWS);
		//返回数据
		model.addAttribute("productList",result.getProductList());
		model.addAttribute("page",page);
		model.addAttribute("totalPages",result.getPageCount());
		//页面展现用户名
		model.addAttribute("user",user);
		
		return "device";
	}
	/**
	 * 保存温度数据
	 * @param productId 产品id 唯一
	 * @param temp1
	 * @param temp2
	 * @param temp3
	 * @param temp4
	 * @param temp5
	 * @return  FlyjavaResult
	 */
	@RequestMapping("/save/temp/{productId}/{temp1}/{temp2}/{temp3}/{temp4}/{temp5}")
	@ResponseBody
	public FlyjavaResult saveTemp(@PathVariable Long productId, 
			@PathVariable float temp1,@PathVariable float temp2,@PathVariable float temp3,
			@PathVariable float temp4,@PathVariable float temp5) throws Exception{
		//判断每个温度值是否为正常温度范围
		if(TEMP_UPPER_LIMIT<temp1||TEMP_UPPER_LIMIT<temp2||TEMP_UPPER_LIMIT<temp3||TEMP_UPPER_LIMIT<temp4||TEMP_UPPER_LIMIT<temp5){
			return FlyjavaResult.build(400, "温度超过最高温度");
		}
		if(TEMP_LOWER_LIMIT>temp1||TEMP_LOWER_LIMIT>temp2||TEMP_LOWER_LIMIT>temp3||TEMP_LOWER_LIMIT>temp4||TEMP_LOWER_LIMIT>temp5){
			return FlyjavaResult.build(400, "温度低于最低温度");
		}
		//创建TbData对象
		TbData tbData=new TbData();
		//创建Temp对象
		Temp temp=new Temp();
		
		//temp 补全pojo
		temp.setTemp1(temp1);
		temp.setTemp2(temp2);
		temp.setTemp3(temp3);
		temp.setTemp4(temp4);
		temp.setTemp5(temp5);
		
		//tbData 补全pojo
		tbData.setProductId(productId);
		//将Temp对象转化成json字符串保存到 TbData对象的Data属性
		tbData.setData(JsonUtils.objectToJson(temp));
		tbData.setProductCatId(TEMP_PRODUCT_CAT_ID);
		tbData.setCreated(new Date());
		//进行保存数据
		FlyjavaResult result = dataService.saveDataByRedis(tbData, REDIS_KEY_TEMP, REDIS_LENGTH_TEMP);
		//判断是否保存成功
		if(result.getStatus()==400){
			return FlyjavaResult.build(400, "保存数据失败");
		}
		/*return FlyjavaResult.ok("save successfully!");*/
		return FlyjavaResult.ok("cmd:sleep time 0003min end");
	}
	
	/*
	 * 展示数据页面  有图表 (图表数据是进行ajax异步请求得到并展示的)
	 * 将获取到的产品ID 返回给页面
	 */
	@RequestMapping("/showData")
	public String showData(@RequestParam Long productId,Model model,HttpServletRequest request){
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//返回页面  用来显示用户名
		model.addAttribute("user", user);
		//用来查询数据
		model.addAttribute("productId", productId);
		return "data";
	}


	/**
	 * 根据产品ID 展现温度数据 默认展现20条
	 * AJAX post请求
	 * @param productId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/data/showTemp",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String showTempByProductId(@RequestParam Long productId) throws Exception{
		//判断productId
		if(productId==null){
			return "";
		}
		//调用服务
		EchartTempResult echartTempResult = dataService.getEchartTempResultByProductId(productId,REDIS_KEY_TEMP, REDIS_LENGTH_TEMP);
		//判断返回值
		if(null==echartTempResult){
			return "";
		}
		
		//返回结果
		String Json = JsonUtils.objectToJson(echartTempResult);
		return Json;
	}

	@RequestMapping(value="/data/searchTemp",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String searchTempByProductIdBetweenStartAndEnd(@RequestParam Long productId,
			@RequestParam String start,@RequestParam String end) throws Exception{
		//判断参数
		if(productId==null){
			return "";
		}
		if(StringUtils.isBlank(start)){
			return "";
		}
		if(StringUtils.isBlank(end)){
			return "";
		}
		//调用服务层
		EchartTempResult echartTempResult = dataService.getEchartTempResultBetweenStartAndEnd(productId, start, end);
		//判断返回值
		if(null==echartTempResult){
			return "";
		}
		
		//返回结果
		String Json = JsonUtils.objectToJson(echartTempResult);
		return Json;
	}
	
	/**
	 * 保存GPS信息
	 * 位置/ID号/信息头/纬度ddmm.mmmmm（度分）/纬度半球N（北半球）或S（南半球）/经度dddmm.mmmmm（度分）/经度半球E（东经）或W（西经）/UTC时间：hhmmss（时分秒）/定位状态，A=有效定位，V=无效定位
	 * /save/location/10018012500005/$GPGLL/4544.92444/N/12643.08448/E/085304.00/A
	 */
	@RequestMapping(value="/save/location/{productId}/{sign}/{latitude}/{latitudeSource}/{longitude}/{longitudeSource}/{UTCtime}/{status}",
			method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FlyjavaResult saveLocationByProductId(@PathVariable Long productId,
			@PathVariable String sign,@PathVariable String latitude,
			@PathVariable String latitudeSource,@PathVariable String longitude,
			@PathVariable String longitudeSource,@PathVariable String UTCtime,
			@PathVariable String status) throws Exception{
		//0.初始化 Double的经纬度 用于判断 和转换
		Double longitudeDouble;
		Double latitudeDouble;
		
		//1.判断参数 是否为空
		//1.1产品Id
		if(productId==null&&productId==0){
			return FlyjavaResult.build(400, "productId不能为空");
		}
		//1.2标识
		if(StringUtils.isNotBlank(sign)){
			if(!StringUtils.contains(sign,"$GPGLL")){//StringUtils.contains(原字符串,需要在原字符串中检测的)
				return FlyjavaResult.build(400, "标识应该以$GPGLL开头");
			}
		}else{
			return FlyjavaResult.build(400, "标识不能为空");
		}
		//1.3 纬度
		if(StringUtils.isBlank(latitude)){
			return FlyjavaResult.build(400, "纬度不能为空");
		}
		//1.4纬度来源
		if(StringUtils.isBlank(latitudeSource)){
			return FlyjavaResult.build(400, "纬度来源不能为空");
		}
		//1.5经度
		if(StringUtils.isBlank(longitude)){
			return FlyjavaResult.build(400, "经度不能为空");
		}
		//1.6经度来源
		if(StringUtils.isBlank(longitudeSource)){
			return FlyjavaResult.build(400, "经度来源不能为空");
		}
		//1.7 UTC 时间
		if(StringUtils.isBlank(UTCtime)){
			return FlyjavaResult.build(400, "UTC不能为空");
		}
		//1.8 状态
		if(StringUtils.isBlank(status)){
			return FlyjavaResult.build(400, "GPS状态不能为空");
		}
		
		//2.判断定位状态 status，A=有效定位，V=无效定位
		if("V".equals(status)){
			return FlyjavaResult.build(400, "无效定位");
		}
		//3.判断经纬度是否有效范围 经度73-135  纬度3-53
		try {
			//3.1将String 转换成Double   4544.92444/N/12643.08448
			longitudeDouble =Double.parseDouble(longitude);
			latitudeDouble =Double.parseDouble(latitude);
			//3.2 纬度 latitude 3-53
			if(latitudeDouble<300||latitudeDouble>5300){
				return FlyjavaResult.build(400, "经度输入有误或不在中国");
			}
			//3.3 经度longitude  73-135
			if(longitudeDouble<7300||longitudeDouble>13500){
				return FlyjavaResult.build(400, "纬度输入有误或不在中国");
			}
		} catch (Exception e) {
			return FlyjavaResult.build(400, "经度或纬度格式有误");
		}
		
		//4.将精度度信息进行转换成度 31.797915°
		longitudeDouble= convertGPSToMap(longitudeDouble);
		latitudeDouble= convertGPSToMap(latitudeDouble);
		//5.创建pojo对象
		//5.1 location对象
		Location gps=new Location();
		gps.setLongitude(longitudeDouble);
		gps.setLatitude(latitudeDouble);
		//5.2创建TbData对象
		TbData tbData=new TbData();
		tbData.setProductId(productId);
		tbData.setProductCatId(GPS_PRODUCT_CAT_ID);
		tbData.setCreated(new Date());
		tbData.setData(JsonUtils.objectToJson(gps));
		
		//6.调用服务层保存数据库
		FlyjavaResult result= dataService.saveDataByRedis(tbData, REDIS_KEY_GPS, REDIS_LENGTH_GPS);
		//7.返回保存成功
		return result;
	}

	/**
	 * 将原始的GPS坐标转换成度表示的坐标， 可以在百度地图上使用
	 * *GPS 串口读出的是 DDMM.MMMM格式 
     *一般上位机是 DD.DDDDDD°或 DD°MM'SS" 格式, 这两种都可以在 GE 里直接输入 
 	 *举例说明: 3147.8749 (示例,经纬度一样) 格式为 DDMM.MMMM  
	 *转换成度:  
	 *1. 度的部分直接就是31,  
	 *2.剩下的 MM.MMMM/60=度, 所以 47.8749/60=0.797915  
	 *则 转换成度是 31.797915°  
	 * @param longitudeDouble
	 * @return
	 */
	private Double convertGPSToMap(Double Source) {
		//0.判断是否为空
		if(Source==null&&Source==0D){
			return 0D;
		}
		//1.初始化变量
		Double target;//返回值
		Double temp;//临时值
		int i1;    //整数部分
		Double d2;    //小数部分
		
		//2.获取整数部分
		temp=Source/100;
		i1=temp.intValue();
		
		//3.获取小数部分
		//3.1 获取除去整数部分的剩下的数值  例如：从 12643.08448 获取43.08448
		d2=Source-i1*100;
		//3.2 小数部分转成度
		d2=d2/60;
		
		//4.合成一个返回值
		target=i1+d2;
		return target;
	}
	/**
	 * 展示位置信息
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/data/showLocation",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String showLocationByProductId(@RequestParam Long productId) throws Exception{
		return null;
	}
	
	/**
	 * 粮仓温湿度接收 20个   0-19
	 */
	@RequestMapping(value="/save/granary/{productId}/{T0}/{H0}/{T1}/{H1}/{T2}/{H2}/{T3}/{H3}/{T4}/{H4}/{T5}/{H5}/{T6}/{H6}/{T7}/{H7}/{T8}/{H8}/{T9}/{H9}/{T10}/{H10}/{T11}/{H11}/{T12}/{H12}/{T13}/{H13}/{T14}/{H14}/{T15}/{H15}/{T16}/{H16}/{T17}/{H17}/{T18}/{H18}/{T19}/{H19}",
			method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FlyjavaResult saveGranary20ByProductId(@PathVariable Long productId,
			@PathVariable Float T0,@PathVariable Float H0,
			@PathVariable Float T1,@PathVariable Float H1,@PathVariable Float T2,@PathVariable Float H2,
			@PathVariable Float T3,@PathVariable Float H3,@PathVariable Float T4,@PathVariable Float H4,
			@PathVariable Float T5,@PathVariable Float H5,@PathVariable Float T6,@PathVariable Float H6,
			@PathVariable Float T7,@PathVariable Float H7,@PathVariable Float T8,@PathVariable Float H8,
			@PathVariable Float T9,@PathVariable Float H9,@PathVariable Float T10,@PathVariable Float H10,
			@PathVariable Float T11,@PathVariable Float H11,@PathVariable Float T12,@PathVariable Float H12,
			@PathVariable Float T13,@PathVariable Float H13,@PathVariable Float T14,@PathVariable Float H14,
			@PathVariable Float T15,@PathVariable Float H15,@PathVariable Float T16,@PathVariable Float H16,
			@PathVariable Float T17,@PathVariable Float H17,@PathVariable Float T18,@PathVariable Float H18,
			@PathVariable Float T19,@PathVariable Float H19) throws Exception{
		//1 非空判断
		if(productId==null){
			return FlyjavaResult.build(500, "设备Id不能为空");
		}
		if(T0==null||T1==null||T2==null||T3==null||T4==null||T5==null||T6==null||T7==null||T8==null||T9==null||T10==null){
			return FlyjavaResult.build(500, "温度不能为空");
		}
		if(H0==null||H1==null||H2==null||H3==null||H4==null||H5==null||H6==null||H7==null||H8==null||H9==null||H10==null){
			return FlyjavaResult.build(500, "湿度不能为空");
		}
		//2.温湿度 值为-256 赋值为空
		if(T0==-256){
			T0=null;
		}
		if(T1==-256){
			T1=null;
		}
		if(T2==-256){
			T2=null;
		}
		if(T3==-256){
			T3=null;
		}
		if(T4==-256){
			T4=null;
		}
		if(T5==-256){
			T5=null;
		}
		if(T6==-256){
			T6=null;
		}
		if(T7==-256){
			T7=null;
		}
		if(T8==-256){
			T8=null;
		}
		if(T9==-256){
			T9=null;
		}
		if(T10==-256){
			T10=null;
		}
		if(T11==-256){
			T11=null;
		}
		if(T12==-256){
			T12=null;
		}
		if(T13==-256){
			T13=null;
		}
		if(T14==-256){
			T14=null;
		}
		if(T15==-256){
			T15=null;
		}
		if(T16==-256){
			T16=null;
		}
		if(T17==-256){
			T17=null;
		}
		if(T18==-256){
			T18=null;
		}
		if(T19==-256){
			T19=null;
		}
		
		if(H0==-256){
			H0=null;
		}
		if(H1==-256){
			H1=null;
		}
		if(H2==-256){
			H2=null;
		}
		if(H3==-256){
			H3=null;
		}
		if(H4==-256){
			H4=null;
		}
		if(H5==-256){
			H5=null;
		}
		if(H6==-256){
			H6=null;
		}
		if(H7==-256){
			H7=null;
		}
		if(H8==-256){
			H8=null;
		}
		if(H9==-256){
			H9=null;
		}
		if(H10==-256){
			H10=null;
		}
		if(H11==-256){
			H11=null;
		}
		if(H12==-256){
			H12=null;
		}
		if(H13==-256){
			H13=null;
		}
		if(H14==-256){
			H14=null;
		}
		if(H15==-256){
			H15=null;
		}
		if(H16==-256){
			H16=null;
		}
		if(H17==-256){
			H17=null;
		}
		if(H18==-256){
			H18=null;
		}
		if(H19==-256){
			H19=null;
		}
		
		//3.保存于Granary对象
		Granary20 granary=new Granary20();
		granary.setT0(T0);
		granary.setT1(T1);
		granary.setT2(T2);
		granary.setT3(T3);
		granary.setT4(T4);
		granary.setT5(T5);
		granary.setT6(T6);
		granary.setT7(T7);
		granary.setT8(T8);
		granary.setT9(T9);
		granary.setT10(T10);
		granary.setT11(T11);
		granary.setT12(T12);
		granary.setT13(T13);
		granary.setT14(T14);
		granary.setT15(T15);
		granary.setT16(T16);
		granary.setT17(T17);
		granary.setT18(T18);
		granary.setT19(T19);
		
		granary.setH0(H0);
		granary.setH1(H1);
		granary.setH2(H2);
		granary.setH3(H3);
		granary.setH4(H4);
		granary.setH5(H5);
		granary.setH6(H6);
		granary.setH7(H7);
		granary.setH8(H8);
		granary.setH9(H9);
		granary.setH10(H10);
		granary.setH11(H11);
		granary.setH12(H12);
		granary.setH13(H13);
		granary.setH14(H14);
		granary.setH15(H15);
		granary.setH16(H16);
		granary.setH17(H17);
		granary.setH18(H18);
		granary.setH19(H19);
		
		//4.创建 tbdata对象
		TbData tbData=new TbData();
		tbData.setCreated(new Date());
		tbData.setData(JsonUtils.objectToJson(granary));
		tbData.setProductId(productId);
		
		//5.保存到redis
		dataService.saveDataByRedis(tbData, REDIS_KEY_GRANARY, REDIS_LENGTH_GRANARY);
		
		//6.返回保存成功
		return FlyjavaResult.ok();
	}
	
	
	/**
	 * 粮仓温湿度接收 11个
	 */
	@RequestMapping(value="/save/granary/{productId}/{T0}/{H0}/{T1}/{H1}/{T2}/{H2}/{T3}/{H3}/{T4}/{H4}/{T5}/{H5}/{T6}/{H6}/{T7}/{H7}/{T8}/{H8}/{T9}/{H9}/{T10}/{H10}",
			method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FlyjavaResult saveGranaryByProductId(@PathVariable Long productId,
			@PathVariable Float T0,@PathVariable Float H0,
			@PathVariable Float T1,@PathVariable Float H1,@PathVariable Float T2,@PathVariable Float H2,
			@PathVariable Float T3,@PathVariable Float H3,@PathVariable Float T4,@PathVariable Float H4,
			@PathVariable Float T5,@PathVariable Float H5,@PathVariable Float T6,@PathVariable Float H6,
			@PathVariable Float T7,@PathVariable Float H7,@PathVariable Float T8,@PathVariable Float H8,
			@PathVariable Float T9,@PathVariable Float H9,@PathVariable Float T10,@PathVariable Float H10) throws Exception{
		//1 非空判断
		if(productId==null){
			return FlyjavaResult.build(500, "设备Id不能为空");
		}
		if(T0==null||T1==null||T2==null||T3==null||T4==null||T5==null||T6==null||T7==null||T8==null||T9==null||T10==null){
			return FlyjavaResult.build(500, "温度不能为空");
		}
		if(H0==null||H1==null||H2==null||H3==null||H4==null||H5==null||H6==null||H7==null||H8==null||H9==null||H10==null){
			return FlyjavaResult.build(500, "湿度不能为空");
		}
		//2.温湿度 值为-256 赋值为空
		if(T0==-256){
			T0=null;
		}
		if(T1==-256){
			T1=null;
		}
		if(T2==-256){
			T2=null;
		}
		if(T3==-256){
			T3=null;
		}
		if(T4==-256){
			T4=null;
		}
		if(T5==-256){
			T5=null;
		}
		if(T6==-256){
			T6=null;
		}
		if(T7==-256){
			T7=null;
		}
		if(T8==-256){
			T8=null;
		}
		if(T9==-256){
			T9=null;
		}
		if(T10==-256){
			T10=null;
		}
		
		if(H0==-256){
			H0=null;
		}
		if(H1==-256){
			H1=null;
		}
		if(H2==-256){
			H2=null;
		}
		if(H3==-256){
			H3=null;
		}
		if(H4==-256){
			H4=null;
		}
		if(H5==-256){
			H5=null;
		}
		if(H6==-256){
			H6=null;
		}
		if(H7==-256){
			H7=null;
		}
		if(H8==-256){
			H8=null;
		}
		if(H9==-256){
			H9=null;
		}
		if(H10==-256){
			H10=null;
		}
		
		//3.保存于Granary对象
		Granary granary=new Granary();
		granary.setT0(T0);
		granary.setT1(T1);
		granary.setT2(T2);
		granary.setT3(T3);
		granary.setT4(T4);
		granary.setT5(T5);
		granary.setT6(T6);
		granary.setT7(T7);
		granary.setT8(T8);
		granary.setT9(T9);
		granary.setT10(T10);
		
		granary.setH0(H0);
		granary.setH1(H1);
		granary.setH2(H2);
		granary.setH3(H3);
		granary.setH4(H4);
		granary.setH5(H5);
		granary.setH6(H6);
		granary.setH7(H7);
		granary.setH8(H8);
		granary.setH9(H9);
		granary.setH10(H10);
		
		//4.创建 tbdata对象
		TbData tbData=new TbData();
		tbData.setCreated(new Date());
		tbData.setData(JsonUtils.objectToJson(granary));
		tbData.setProductId(productId);
		
		//5.保存到redis
		dataService.saveDataByRedis(tbData, REDIS_KEY_GRANARY, REDIS_LENGTH_GRANARY);
		
		//6.返回保存成功
		return FlyjavaResult.ok();
	}
	
	/**
	 * 展示粮仓温度 Temperature
	 */
	@RequestMapping(value="/data/showGranaryTemperature",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String showGranaryTemperature(@RequestParam Long productId) throws Exception{
		//0 判断ID是否为空，有效性
		if(productId==null){
			return null;
		}
		//1.根据ID去Redis查询数据
		EchartGranaryResult echartGranaryResult=dataService.getEchartGranaryTemperatureByProductId(productId, REDIS_KEY_GRANARY, REDIS_LENGTH_GRANARY);
		
		//判断返回值
		if(null==echartGranaryResult){
			return "";
		}
		
		//返回结果
		String Json = JsonUtils.objectToJson(echartGranaryResult);
		return Json;
	}
	
	/**
	 * 展示粮仓湿度 Humidity
	 */
	@RequestMapping(value="/data/showGranaryHumidity",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String showGranaryHumidity(@RequestParam Long productId) throws Exception{
		//0 判断ID是否为空，有效性
		if(productId==null){
			return null;
		}
		//1.根据ID去Redis查询数据
		EchartGranaryResult echartGranaryResult=dataService.getEchartGranaryHumidityByProductId(productId, REDIS_KEY_GRANARY, REDIS_LENGTH_GRANARY);
		
		//判断返回值
		if(null==echartGranaryResult){
			return "";
		}
		
		//返回结果
		String Json = JsonUtils.objectToJson(echartGranaryResult);
		return Json;
	}
	
	/**
	 * 表格展示粮仓温湿度   最新的一组数据
	 */
	@RequestMapping(value="/data/showGranaryTables",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String showGranaryTables(@RequestParam Long productId) throws Exception{
		//0 判断ID是否为空，有效性
		if(productId==null){
			return null;
		}
		//1.根据ID去Redis查询数据
		GranaryTables granaryTables=dataService.getGranaryTablesByProductId(productId, REDIS_KEY_GRANARY, 1);
		
		//判断返回值
		if(null==granaryTables){
			return "";
		}
		
		//返回结果
		String Json = JsonUtils.objectToJson(granaryTables);
		return Json;
	}
	
	/**
	 * 搜索粮仓温湿度
	 */
	@RequestMapping(value="/data/searchGranary",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String searchGranaryByProductIdBetweenStartAndEnd(@RequestParam Long productId,
			@RequestParam String start,@RequestParam String end) throws Exception{
		//判断参数
		if(productId==null){
			return "";
		}
		if(StringUtils.isBlank(start)){
			return "";
		}
		if(StringUtils.isBlank(end)){
			return "";
		}
		//调用服务层
		EchartSearchGranaryResult echartSearchGranaryResult = dataService.getEchartGranaryResultBetweenStartAndEnd(productId, start, end);
		//判断返回值
		if(null==echartSearchGranaryResult){
			return "";
		}
		
		//返回结果
		String Json = JsonUtils.objectToJson(echartSearchGranaryResult);
		return Json;
	}
}
