package com.fooengineers.projetoAcVansV4.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	public void save(String key, String value, long ttlSeconds) {
		redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
	}
	
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	public boolean exists(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
	
	public void delete(String key) {
		redisTemplate.delete(key);
	}
}
