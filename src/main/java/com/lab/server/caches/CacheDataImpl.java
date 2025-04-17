package com.lab.server.caches;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class CacheDataImpl<T> implements ICacheData<T> {
	
	private final RedisTemplate<String, T> redisTemplate;

	@Override
	public T find(String key) {
		 return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void save(String key, T data, long time) {
		redisTemplate.opsForValue().set(key, data);
        redisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
	}

	@Override
	public void delete(String key) {
		 redisTemplate.delete(key);
	}

	@Override
	public void deleteAll() {
		redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("*")));
	}

	@Override
	public void update(String key, T data) {
		redisTemplate.opsForValue().set(key, data);
	}

}

