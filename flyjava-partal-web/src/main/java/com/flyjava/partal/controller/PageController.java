package com.flyjava.partal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@Value("${INDEX_URL}")
	String IndexUrl;
	
	@RequestMapping("/index")
	public String showPage(){
		//跳转 www.ismartlab.cn
		return "redirect:"+IndexUrl;
	}
	
}
