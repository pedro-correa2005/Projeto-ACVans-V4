package com.fooengineers.projetoAcVansV4.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private RedisTemplate<String, Integer> integerRedisTemplate;

	
	public void saveRefreshToken(String jti, String username, long createdAt, long ttlSeconds) {
		String value = username + "|" + createdAt;
		stringRedisTemplate.opsForValue().set(jti, value, ttlSeconds, TimeUnit.SECONDS);
	}
	
	public void save2FACode(String email, String code, long ttlSeconds) {
		stringRedisTemplate.opsForValue().set(email, code, ttlSeconds, TimeUnit.SECONDS);
	}
	
	public void save2FASession(String tempToken, String email, long ttlSeconds) {
		stringRedisTemplate.opsForValue().set(tempToken, email, ttlSeconds, TimeUnit.SECONDS);
	}
	
	public void save2FAAttempts(String tempToken, long ttlSeconds) {
		String key = "2fa_attempts:" + tempToken;
		integerRedisTemplate.opsForValue().set(key, 0, ttlSeconds, TimeUnit.SECONDS);
	}
	
	public Long increment2FAAttempts(String tempToken) {
		String key = "2fa_attempts:" + tempToken;
		return integerRedisTemplate.opsForValue().increment(key);
	}
	
	public Long increment(String key, long ttlSeconds) {
		Long attempts = integerRedisTemplate.opsForValue().increment(key);
		if(attempts == 1) {
			integerRedisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
		}
		
		return attempts;
	}
	
	public long getCreatedAt(String jti) {
		String value = stringRedisTemplate.opsForValue().get(jti);
		if (value == null) return 0;
		String[] parts = value.split("\\|");
		return Long.parseLong(parts[1]);
	}
	
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}
	
	public boolean exists(String key) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
	}
	
	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}
}
