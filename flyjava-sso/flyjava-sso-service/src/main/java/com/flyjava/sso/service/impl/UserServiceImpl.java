package com.flyjava.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flyjava.sso.dao.TbUserMapper;
import com.flyjava.sso.pojo.TbUser;
import com.flyjava.sso.pojo.TbUserExample;
import com.flyjava.sso.pojo.TbUserExample.Criteria;
import com.flyjava.redis.JedisClient;
import com.flyjava.sso.service.UserService;
import com.flyjava.utils.JsonUtils;
import com.flyjava.utils.FlyjavaResult;

@Service
public class UserServiceImpl implements UserService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);  
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	
	@Override
	public FlyjavaResult check(String param, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 判断type
		// 1代表username
		if (1 == type) {
			criteria.andUsernameEqualTo(param);
		}
		// 2代表phone
		else if (2 == type) {
			criteria.andPhoneEqualTo(param);
		}
		// 3代表email
		else if (3 == type) {
			criteria.andEmailEqualTo(param);
		} else {
			return FlyjavaResult.build(400, "参数有非法数据", false);
		}

		// 分别去数据库查询
		List<TbUser> list = userMapper.selectByExample(example);
		// 判断返回集合是否为空
		if (null != list && list.size() > 0) {
			return FlyjavaResult.build(400, "数据已存在", false);
		}
		// 数据可以使用
		return FlyjavaResult.build(200, "ok", true);
	}

	@Override
	public FlyjavaResult register(TbUser user) {
		// 检查数据的有效性
		if (StringUtils.isBlank(user.getUsername())) {
			return FlyjavaResult.build(400, "用户名不能为空");
		}
		// 检查用户名是否重复 ture 数据可以用 false 说明数据库存在
		if (!(boolean) check(user.getUsername(), 1).getData()) {
			return FlyjavaResult.build(400, "用户名或密码错误");
		}
		// 检查密码是否为空
		if (StringUtils.isBlank(user.getPassword())) {
			return FlyjavaResult.build(400, "密码不能为空");
		}
		// 检查手机是否为空
		if (StringUtils.isBlank(user.getPhone())) {
			return FlyjavaResult.build(400, "手机号不能为空");
		} else {
			// 检查手机是否有效 ture 数据可以用 false 说明数据库存在
			if (!(boolean) check(user.getPhone(), 2).getData()) {
				return FlyjavaResult.build(400, "手机号码已注册");
			}
		}
		// 检查email是否为空 如果不为空那就进行有效性判断
		if (StringUtils.isNotBlank(user.getEmail())) {
			// 检查email是否有效 ture 数据可以用 false 说明数据库存在
			if (!(boolean) check(user.getEmail(), 3).getData()) {
				return FlyjavaResult.build(400, "邮箱已注册");
			}
		}
		// 补全pojo属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 密码MD5加密保存
		String md5Hex = DigestUtils.md5Hex(user.getPassword());
		user.setPassword(md5Hex);
		// 插入数据
		userMapper.insert(user);
		// 返回成功
		return FlyjavaResult.ok();
	}
	
	@Override
	public FlyjavaResult login(String username, String password) {
		// 检查用户名是否存在
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list==null||list.size()==0){
			//返回登陆失败
			return FlyjavaResult.build(400, "用户名或密码错误");
		}
		// 将密码加密后与数据库密码进行对比
		TbUser user = list.get(0);
		String md5Hex = DigestUtils.md5Hex(password);
		if(!user.getPassword().equals(md5Hex)){
			return FlyjavaResult.build(400, "用户名或密码错误");
		}
		logger.info("用户名密码正确");
		// 成功后生成token
		String token=UUID.randomUUID().toString();
		// 清空密码,保证安全
		// 写入redis数据库
		jedisClient.set(USER_SESSION+":"+token, JsonUtils.objectToJson(user));
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		// 返回token
		return FlyjavaResult.ok(token);
	}
	
	@Override
	public FlyjavaResult getUserByToken(String token) {
		//从redis取出用户数据
		String strJson = jedisClient.get(USER_SESSION+":"+token);
		if(StringUtils.isBlank(strJson)){
			return FlyjavaResult.build(400, "用户登录已过期");
		}
		//重新设置有效时间
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		//转换json
		TbUser user = JsonUtils.jsonToPojo(strJson, TbUser.class);
		return FlyjavaResult.ok(user);
	}

	/**
	 * 退出登录
	 */
	@Override
	public FlyjavaResult loginOutByToken(String token) {
		//删除redis的 信息 -1：永久 -2 ：不存在
		jedisClient.expire(USER_SESSION+":"+token, -2);
		
		return FlyjavaResult.ok();
	}

	/**
	 * 更新用户数据  
	 * 
	 * 从数据库取出原来的user
	 * 将传来的 不为空的数据更新
	 * 再将取出来的user更新到数据库
	 */
	@Override
	public FlyjavaResult update(TbUser user) {
		TbUser oldUser = userMapper.selectByPrimaryKey(user.getId());
		//判断user的字段
		
		//判断密码不为空
		if(StringUtils.isNotBlank(user.getPassword()) ){
			// 密码MD5加密保存
			String md5Hex = DigestUtils.md5Hex(user.getPassword());
			oldUser.setPassword(md5Hex);
		}
		//判断用户名  不为空 就更新
		if(StringUtils.isNotBlank(user.getUsername()) ){
			oldUser.setUsername(user.getUsername());
		}
		//判断邮箱  不为空 就更新
		if(StringUtils.isNotBlank(user.getEmail())) {
			oldUser.setEmail(user.getEmail());
		}
		//判断手机  不为空 就更新
		if(StringUtils.isNotBlank(user.getPhone())) {
			oldUser.setPhone(user.getPhone());
		}
		//更新时间
		oldUser.setUpdated(user.getUpdated());  
		
		//数据库更新
		userMapper.updateByPrimaryKey(oldUser);
		return FlyjavaResult.ok();
	}

}
