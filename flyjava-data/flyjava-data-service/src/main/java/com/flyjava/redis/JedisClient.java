package com.flyjava.redis;

import java.util.List;

public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
	
	//list相关
	Long llen(String key);
	Long rpush(String key, String... strings);
	String lpop(String key);
	List<String> lrange(String key,long start,long end);
}
