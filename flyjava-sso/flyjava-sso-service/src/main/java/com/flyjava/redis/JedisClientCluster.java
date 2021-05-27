package com.flyjava.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;

public class JedisClientCluster implements JedisClient {
	
	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public Long hdel(String key, String... field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public Long llen(String key) {
		
		return jedisCluster.llen(key);
	}

	@Override
	public Long rpush(String key, String... strings) {
		
		return jedisCluster.rpush(key, strings);
	}

	@Override
	public String lpop(String key) {
		
		return jedisCluster.lpop(key);
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return jedisCluster.lrange(key, start, end);
	}

}
