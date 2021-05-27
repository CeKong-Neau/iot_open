package com.flyjava.data.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flyjava.data.pojo.TbHostProduct;
import com.flyjava.data.pojo.TbUserHost;
import com.flyjava.data.service.DataService;
import com.flyjava.sso.pojo.TbUser;
import com.flyjava.utils.FlyjavaResult;
/**
 * 关于设备的
 * 	保存主机
 * 	将产品ID与主机绑定
 * @author 彭同学
 *
 */
@Controller
public class ProductController {
	//拦截器 拦截成功在request上的user对象的 key
	@Value("${LOGIN_USER}")
	private String LOGIN_USER;

	@Autowired
	private DataService dataService;
	
	
	/**
	 * 保存主机
	 * Ajax post
	 * @return
	 */
	@RequestMapping(value="/data/saveHost",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult saveUserHost(@RequestParam String hostName,HttpServletRequest request)throws Exception{
		//判断hostName
		if(StringUtils.isBlank(hostName)){
			return FlyjavaResult.build(400, "主机名称不能为空");
		}
		//判断系统时候有相同主机名称的
		FlyjavaResult resultForHost = dataService.checkHostName(hostName);
		//说明没有重复
		if(resultForHost.getStatus()==400){
			return resultForHost;
		}
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		//获取用户ID
		Long userId = user.getId();
		//创建TbUserHost
		TbUserHost tbUserHost = new TbUserHost();
		//补全pojo
		tbUserHost.setHostName(hostName);
		tbUserHost.setUserId(userId);
		tbUserHost.setStatus((byte) 1);
		tbUserHost.setCreated(new Date());
		tbUserHost.setUpdated(new Date());
		//调用服务
		FlyjavaResult result =dataService.saveUserHost(tbUserHost);
		return result;
		
	}
	
	/**
	 * 检查主机名称是否被占用
	 * Ajax post
	 * @return
	 */
	@RequestMapping(value="/check/hostName",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult checkHostName(@RequestParam String hostName)throws Exception{
		//判断hostName
		if(StringUtils.isBlank(hostName)){
			return FlyjavaResult.build(400, "主机名称不能为空");
		}
		
		
		//调用服务
		FlyjavaResult result =dataService.checkHostName(hostName);
		return result;
	}
	/**
	 * 检查productId是否被占用
	 * Ajax post
	 * @return
	 */
	@RequestMapping(value="/check/productId",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult checkroductId(@RequestParam Long productId)throws Exception{
		//判断hostName
		if(productId==null){
			return FlyjavaResult.build(400, "设备Id不能为空");
		}
		//调用服务
		FlyjavaResult result =dataService.checkProductId(productId);
		return result;
	}
	
	/**
	 * 展示用户的主机列表
	 * Ajax post
	 * @return
	 */
	@RequestMapping(value="/showHostName",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult showHostName(HttpServletRequest request)throws Exception{
		//从request中获取User
		TbUser user=(TbUser) request.getAttribute(LOGIN_USER);
		Long userId=user.getId();
		
		//调用服务
		FlyjavaResult result =dataService.getUserHostByUserId(userId);
		return result;
	}
	
	/**
	 * 展示传感器种类
	 * Ajax post
	 * @return
	 */
	@RequestMapping(value="/showProductCat",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult showProductCat()throws Exception{
		
		//调用服务
		FlyjavaResult result =dataService.getProductCat();
		return result;
	}
	
	/**
	 * 绑定设备到主机
	 * Ajax post
	 * @return
	 */
	@RequestMapping(value="/bindProduct",method=RequestMethod.POST)
	@ResponseBody
	public FlyjavaResult bindProduct(TbHostProduct tbHostProduct)throws Exception{
		//补全pojo
		if(tbHostProduct.getHostId()==null){
			return  FlyjavaResult.build(400, "主机ID不能为空");
		}
		if(tbHostProduct.getProductId()==null){
			return  FlyjavaResult.build(400, "产品ID不能为空");
		}
		if(tbHostProduct.getProductCatId()== 0){
			return  FlyjavaResult.build(400, "产品类别不能为空");
		}
		//补全pojo
		tbHostProduct.setStatus((byte)1);
		tbHostProduct.setCreated(new Date());
		tbHostProduct.setUpdated(new Date());
		//调用服务
		FlyjavaResult result =dataService.saveHostProduct(tbHostProduct);
		return result;
	}
	
	
	
}
