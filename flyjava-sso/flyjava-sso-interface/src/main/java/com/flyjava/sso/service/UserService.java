package com.flyjava.sso.service;

import com.flyjava.sso.pojo.TbUser;
import com.flyjava.utils.FlyjavaResult;

public interface UserService {

	public FlyjavaResult check(String param,Integer type);
	public FlyjavaResult register(TbUser user);
	public FlyjavaResult login(String username,String password);
	public FlyjavaResult getUserByToken(String token);
	//退出登录 通过token
	public FlyjavaResult loginOutByToken(String token);
	
	//更新用户数据 要求用户名不能重复
	public FlyjavaResult update(TbUser user);

}
