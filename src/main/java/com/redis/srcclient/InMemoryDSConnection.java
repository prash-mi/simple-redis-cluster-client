/**
 * 
 */
package com.redis.srcclient;

/*
 * @author prash_mi@yahoo.com
 * 
 * Interface
 * 
 */
public interface InMemoryDSConnection {

	public void putInCache(String key, String value, int... expiry);
	public String getFromCache(String key);
	public void expireKey(String key);
	
	
}
