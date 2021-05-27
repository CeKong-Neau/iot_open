package com.flyjava.data.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.flyjava.sso.pojo.TbUser;
import com.flyjava.sso.service.UserService;
import com.flyjava.utils.CookieUtils;
import com.flyjava.utils.FlyjavaResult;

/**
 * 拦截请求,是否登陆
 * @author 彭同学
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(LoginInterceptor.class);  

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	//拦截通过,存放request域中user对象的名称
	@Value("${LOGIN_USER}")
	private String LOGIN_USER;
	
	@Autowired
	private UserService userService;
	//handler执行前执行该方法
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		//从cookie中获取token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		//没有token,跳转到登陆页面,需要携带当前url
		if(StringUtils.isBlank(token)){
			String url=request.getRequestURL().toString();
			response.sendRedirect(SSO_LOGIN_URL+"?redirectUrl=" + url);
			//拦截
			return false;
		}
		//有token 调用sso系统 根据token获取用户信息
		FlyjavaResult result = userService.getUserByToken(token);
		//查询不到用户信息,跳转到登陆
		if(result.getStatus()!=200){
			String url=request.getRequestURL().toString();
			response.sendRedirect(SSO_LOGIN_URL+"?redirectUrl=" + url);
			logger.info("查询不到用户信息,跳转到登陆:"+url);
			//拦截
			return false;
		}
		TbUser user=(TbUser) result.getData();
		request.setAttribute(LOGIN_USER, user);
		logger.info("查询到 放行");
		//查询到 放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
