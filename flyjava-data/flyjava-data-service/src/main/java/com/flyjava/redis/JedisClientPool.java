package com.flyjava.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClientPool implements JedisClient {
	
	@Autowired
	private JedisPool jedisPool;

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis =jedisPool.getResource();
		String result =jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis =jedisPool.getResource();
		Boolean result =jedis.exists(key);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis =jedisPool.getResource();
		Long result =jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis =jedisPool.getResource();
		Long result =jedis.ttl(key);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis =jedisPool.getResource();
		Long result =jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis =jedisPool.getResource();
		Long result =jedis.hset(key, field, value);
		jedis.close();
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis =jedisPool.getResource();
		String result =jedis.hget(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis =jedisPool.getResource();
		Long result =jedis.hdel(key, field);
		jedis.close();
		return result;
	}
	
	//list相关
	@Override
	public Long llen(String key) {
		Jedis jedis =jedisPool.getResource();
		Long result =jedis.llen(key);
		jedis.close();
		return result;
	}

	@Override
	public Long rpush(String key, String... strings) {
		Jedis jedis =jedisPool.getResource();
		Long result =jedis.rpush(key,strings);
		jedis.close();
		return result;
	}

	@Override
	public String lpop(String key) {
		Jedis jedis =jedisPool.getResource();
		String result =jedis.lpop(key);
		jedis.close();
		return result;
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis =jedisPool.getResource();
		List<String> result=jedis.lrange(key,start,end);
		jedis.close();
		return result;
	}

}
