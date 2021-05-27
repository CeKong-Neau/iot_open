package com.flyjava.data.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flyjava.sso.pojo.TbUser;
import com.flyjava.sso.service.UserService;
/**
 * 关于个人信息更改的
 * 	展示个人信息
 * 	展示更改密码页面
 * @author 彭同学
 *
 */
@Controller
public class AccountController {
	//拦截器 拦截成功在request上的user对象的 key
	@Value("${LOGIN_USER}")
	private String LOGIN_USER;

	@Autowired
	private UserService userService;
	/**
	 * 展示个人信息
	 * 需要把user传递给页面  用于显示用户名
	 * @return
	 */
	@RequestMapping("/account")
	public String showAccount(HttpServletRequest request,Model model){
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//返回页面
		model.addAttribute("user", user);
		return "account";
	}
	/**
	 * 展示更改密码页面
	 * 需呀把user传递给页面
	 * @return
	 */
	@RequestMapping("/password")
	public String showPassword(HttpServletRequest request,Model model){
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//返回页面
		model.addAttribute("user", user);
		return "password";
	}
	
}
