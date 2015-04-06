package com.redis.srcclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.HostAndPort;

/*
 * @author prash_mi@yahoo.com
 * 
 * This class spawns pooled connection to each redis host
 * and implements the required methods for setting and
 * retrieving key-value pairs for the redis cluster 
 * 
 */

public class SRCConnection implements InMemoryDSConnection{

	private List<PooledConnection> redisPooledConnections;
	private Set<HostAndPort> redisHosts = 	SRCCConstants.jedisClusterNodes;
	private int buckets;

	public SRCConnection(){//Initialize this class while initializing pooled connection with all the hosts
		buckets = redisHosts.size();
		redisPooledConnections = new ArrayList<PooledConnection>();

		Iterator<HostAndPort> hIterator =  redisHosts.iterator();
		while(hIterator.hasNext()){//Initialize the connection pool for the mentioned hosts
			HostAndPort hp = hIterator.next();
			System.out.println("Initializing pool >>");
			redisPooledConnections.add(new PooledConnection(hp.getHost(), hp.getPort()));
		}


	}

	@Override
	public void putInCache(String key, String value, int... expiry) {//puts a key in cache
		redisPooledConnections.get(getBucketNumber(key)).putInCache(key, value, expiry);

	}

	@Override
	public String getFromCache(String key) {//gets a key from the cache
		return redisPooledConnections.get(getBucketNumber(key)).getFromCache(key);
	}

	@Override
	public void expireKey(String key) {//expires a key

		redisPooledConnections.get(getBucketNumber(key)).expireKey(key);

	}


	private int getBucketNumber(String key){//get bucket number for each key
		
		//This method returns a bucket number such that bucketNumber >= 0 and bucketNumber<TotalRedisNodes
		//This method uses hascode of the key for computing the bucket number
		
			return ((key.hashCode()<0) ? (-1*key.hashCode()) : key.hashCode() )%buckets;

	}

}
