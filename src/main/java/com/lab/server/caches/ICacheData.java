package com.lab.server.caches;

public interface ICacheData<T> {
	 T find(String key);

	 void save(String key, T data, long time);

	 void delete(String key);

	 void deleteAll();

	 void update(String key, T data);
}