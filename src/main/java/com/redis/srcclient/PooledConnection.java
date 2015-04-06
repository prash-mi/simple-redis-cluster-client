package com.redis.srcclient;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/*
 * @author prash_mi@yahoo.com
 * This class maintains pooled connection with respective hosts
 * 
 */
public class PooledConnection implements InMemoryDSConnection{

	//	private  JedisConnectionPool conFactInstance;
	private  JedisPool jPool;
	private String host;

	protected PooledConnection(String host, int port){//this isnt supposed to be initialized outside of the package

		try {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			/* Some extra configuration */


			//this will raise JedisConnectionException if the pool is exhausted 
			poolConfig.setBlockWhenExhausted(SRCCConstants.POOL_SET_BLOCK_WHEN_EXHAUSTED);

			//Max connections per redis node
			poolConfig.setMaxTotal(SRCCConstants.POOL_SET_MAX_TOTAL);

			// Tests whether connection is dead when connection
			// retrieval method is called
			poolConfig.setTestOnBorrow(SRCCConstants.POOL_SET_TEST_ON_BORROW);


			// Tests whether connection is dead when returning a
			// connection to the pool
			poolConfig.setTestOnReturn(SRCCConstants.POOL_SET_TEST_ON_RETURN);

			// Number of connections to Redis that just sit there
			// and do nothing
			poolConfig.setMaxIdle(SRCCConstants.POOL_SET_MAX_IDLE);
			// Minimum number of idle connections to Redis
			// These can be seen as always open and ready to serve
			poolConfig.setMinIdle(SRCCConstants.POOL_SET_MIN_IDLE);

			// Tests whether connections are dead during idle periods
			poolConfig.setTestWhileIdle(SRCCConstants.POOL_SET_TEST_WHILE_IDLE);

			// Maximum number of connections to test in each idle check
			poolConfig.setNumTestsPerEvictionRun(SRCCConstants.POOL_SET_NUM_TESTS_PER_EVICTION_RUN);

			// Idle connection checking period
			poolConfig.setTimeBetweenEvictionRunsMillis(SRCCConstants.POOL_SET_TIME_BETWEEN_EVICTION_RUNS_MILLIS);

			// Create the jedisPool
			if(SRCCConstants.REDIS_PASSOWORD != null && !SRCCConstants.REDIS_PASSOWORD.trim().equals("")){
				jPool = new JedisPool(poolConfig, host, port, SRCCConstants.REDIS_CONNECTION_TIMEOUT, SRCCConstants.REDIS_PASSOWORD);
			}else{
				jPool = new JedisPool(poolConfig, host, port, SRCCConstants.REDIS_CONNECTION_TIMEOUT);
			}

			this.host = host;

			System.out.println("PooledConnection Initialized for host: "+this.host);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private Jedis getJedis(){//returns a resource
		return this.jPool.getResource();
	}

	@Override
	public void expireKey(String key){//expires a key
		Jedis resource = this.getJedis();
		try{
			resource.expire(key, 0);
		}finally{
			this.jPool.returnResource(resource);
		}


	}



	@Override
	public void putInCache(String key, String value, int... expiry) {//This function puts or overwrites a key
		Jedis resource = this.getJedis();
		try{
			System.out.println("Putting key: "+key+", value: "+value+" in HOST: "+this.host);
			if(expiry.length == 1){//if expiry value is passed
				resource.expire(key, 0);//expire the key first as Jedis cant overwrite the key if an expiry is mentioned 

				/**
				 * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
				 * GB).
				 * @param key
				 * @param value
				 * @param nxxx NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key
				 * if it already exist.
				 * @param expx EX|PX, expire time units: EX = seconds; PX = milliseconds
				 * @param time expire time in the units of {@param #expx}
				 * @return Status code reply
				 */

				resource.set(key, value, "NX", "EX", expiry[0]);
			}else{
				resource.set(key, value);
			}
		}finally{
			this.jPool.returnResource(resource);
		}


	}

	@Override
	public String getFromCache(String key) {//gets a key from cache
		Jedis resource = this.getJedis();
		try{
			String value = resource.get(key);
			System.out.println("Got value: "+value+" from HOST: "+this.host);
			return value;
		}finally{
			this.jPool.returnResource(resource);
		}

	}

	@Override
	protected void finalize() throws Throwable {//Not guaranteed that it will be called, implemented as a best practice 
		try {
			if(this.jPool!=null){
				this.jPool.destroy(); 
			}
			System.out.println("Connection pool destroyed for host: "+this.host);
		} finally {
			super.finalize();
		}
	}

}
