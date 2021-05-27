package com.flyjava.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示登陆和注册页面
 * @author 彭同学
 *
 */
@Controller
public class PageController {

	@RequestMapping("/index")
	public String showPage(){
		return "login";
	}
	@RequestMapping("/user/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/user/login")
	public String showLogin(String redirectUrl,Model model){
		model.addAttribute("redirectUrl", redirectUrl);
		return "login";
	}
}
