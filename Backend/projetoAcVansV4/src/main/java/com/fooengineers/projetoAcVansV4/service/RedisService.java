package com.fooengineers.projetoAcVansV4.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	public void saveRefreshToken(String jti, String username, long createdAt, long ttlSeconds) {
		String value = username + "|" + createdAt;
		redisTemplate.opsForValue().set(jti, value, ttlSeconds, TimeUnit.SECONDS);
	}
	
	public long getCreatedAt(String jti) {
		String value = redisTemplate.opsForValue().get(jti);
		if (value == null) return 0;
		String[] parts = value.split("\\|");
		return Long.parseLong(parts[1]);
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
