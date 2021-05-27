package com.flyjava.sso.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flyjava.sso.pojo.TbUser;
import com.flyjava.sso.service.UserService;
import com.flyjava.utils.CookieUtils;
import com.flyjava.utils.FlyjavaResult;
/**
 * 注册登陆有关的
 *  检查注册 用户名 手机号 邮箱的有效性
 *  登陆
 *  注册
 *  根据token得到User 支持jsonp 拦截器会用到
 *  注销登陆
 *  更新用户信息  支持Jsonp
 *  
 * @author 彭同学
 *
 */
@Controller
public class UserController {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 检测注册的用户名 手机号 邮箱 是否存在
	 *    跨域请求 使用JSONP 请求  请求参数带有callback	jQuery21104341255931241117_1515651381076
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public Object check(@PathVariable String param,@PathVariable Integer type,String callback){
		FlyjavaResult result = userService.check(param, type);
		
		//判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
			//设置回调方法
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
	/**
	 * 注册
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult register(TbUser user){
		FlyjavaResult result = userService.register(user);
		return result;
	}
	/**
	 * 登陆
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult login(@RequestParam(value="username" ) String username,
			@RequestParam(value="password" ) String password,
			HttpServletRequest request,HttpServletResponse response){
		if(username==null){
			return FlyjavaResult.build(400, "用户名不能为空");
		}
		if(password==null){
			return FlyjavaResult.build(400, "密码不能为空");
		}
		FlyjavaResult result = userService.login(username, password);
		//登陆成功后需要写入cookie
		if(200==result.getStatus()){
			//将token写入cookie
			CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
		}
		return result;
	}
	
	/*@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,
	 * produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		TaotaoResult result = userService.getUserByToken(token);
		//判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	//Jsonp第二种方法,Spring4.1以上使用
	//根据用户token 查询User
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		FlyjavaResult result = userService.getUserByToken(token);
		//判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
			//设置回调方法
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
	/*
	 * 根据用户的token退出登陆
	 */
	@RequestMapping("/user/loginOut")
	public String loginOut(HttpServletRequest request,HttpServletResponse response){
		//从request中获取cookie对象
		//从cookie对象获取token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		//调用 服务 删除redis中的缓存
		userService.loginOutByToken(token);
		//删除cookie中的token
		CookieUtils.setCookie(request, response, TOKEN_KEY, "");
		//重定向至登陆页面
		return "redirect:/user/login";
	}
	
	@RequestMapping("/user/update")
	@ResponseBody
	public Object update(TbUser user,String callback){
		//补全pojo
		user.setUpdated(new Date());
		//调用服务
		FlyjavaResult result = userService.update(user);
		//判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
			//设置回调方法
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
