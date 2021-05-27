package com.flyjava.data.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.flyjava.data.pojo.Location;
import com.flyjava.data.pojo.TbData;
import com.flyjava.data.service.DataService;
import com.flyjava.sso.pojo.TbUser;
/**
 * 展示其他页面
 * @author 彭同学
 *
 */
@Controller
public class PageController {
	
	//拦截器 拦截成功在request上的user对象的 key
	@Value("${LOGIN_USER}")
	private String LOGIN_USER;
	
	//保存在redis 中的location 对象的key
	@Value("${REDIS_KEY_GPS}")
	private String REDIS_KEY_GPS;

	//粮仓温湿度设备ID 
	@Value("${GRANARY_PRODUCTID}")
	private Long GRANARY_PRODUCTID;
		

	@Autowired
	private DataService dataService;
	
	//展示首页
	@RequestMapping("/index")
	public String showIndex(HttpServletRequest request,Model model){
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//返回页面  用来显示用户名
		model.addAttribute("user", user);
		return "index";
	}
	//设备管理
	@RequestMapping("/device-manager")
	public String showDeviceManager(HttpServletRequest request,Model model){
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//返回页面  用来显示用户名
		model.addAttribute("user", user);
		return "device-manager";
	}
	//版本信息
	@RequestMapping("/info")
	public String showInfo(HttpServletRequest request,Model model){
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//返回页面  用来显示用户名
		model.addAttribute("user", user);
		return "info";
	}
	
	//粮仓监测
	@RequestMapping("/granary")
	public String showGranary(@RequestParam Long productId, HttpServletRequest request,Model model){
		if(productId==null){
			//如果没有传入粮仓ID 那就默认展示一个粮仓ID的数据
			productId=GRANARY_PRODUCTID;
		}
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//返回页面  用来显示用户名
		model.addAttribute("user", user);
		
		model.addAttribute("productId", productId);
		return "granary";
	}
	
	
	/**
	 * 设备分布 默认将用户所有的GPS设备展示出来  这里的GPS ID对应的是土壤温度传感器
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/map")
	public String showMap(HttpServletRequest request,Model model){
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		
		//根据用户id把所有的土壤温度设备id 查询到
		//接着到redis中根据土壤id查询 location信息
		//最后将 location 信息返回至jsp页面
		
		List<Location> locationList=dataService.getLocationListByUserId(user.getId(),REDIS_KEY_GPS);
		List<TbData> tbDataList=dataService.getTbDataListByUserId(user.getId(),REDIS_KEY_GPS);
		
		//返回location集合
		model.addAttribute("locationList", locationList);
		
		//返回tbData集合
		model.addAttribute("tbDataList", tbDataList);
		
		//返回页面 用来显示用户名
		model.addAttribute("user", user);
		return "map";
	}
	
	
	
	
}
