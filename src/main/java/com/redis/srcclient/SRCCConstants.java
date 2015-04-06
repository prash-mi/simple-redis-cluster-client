package com.redis.srcclient;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import redis.clients.jedis.HostAndPort;

/*
 * @author prash_mi@yahoo.com
 * 
 * Constants file
 * 
 */

public class SRCCConstants {




	private SRCCConstants() {
		//Utility classes should not have a default constructor
	}

	/*Redis Keys and constants start*/
	public final static Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
	public static final String REDIS_PASSOWORD ;
	public static final int REDIS_CONNECTION_TIMEOUT;
	public static final int POOL_SET_MAX_TOTAL;
	public static final int POOL_SET_MAX_IDLE;
	public static final int POOL_SET_MIN_IDLE;
	public static final int POOL_SET_NUM_TESTS_PER_EVICTION_RUN;
	public static final int POOL_SET_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
	public static final boolean POOL_SET_BLOCK_WHEN_EXHAUSTED;
	public static final boolean POOL_SET_TEST_ON_BORROW;
	public static final boolean POOL_SET_TEST_ON_RETURN;
	public static final boolean POOL_SET_TEST_WHILE_IDLE;
	private static final String PROPERTIES_FILE_LOCAL = "srcc";



	/*
	 *Static initialization block
	 * 
	 */


	static{//loads properties from srcc properties file

		ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_LOCAL, Locale.ENGLISH);

		/*Property Loading for Inmemdeb start*/	
		Enumeration<String> keys =  bundle.getKeys();
		while(keys.hasMoreElements()){//load hosts and ports from property file
			String key = keys.nextElement();
			if(key!= null && key.toLowerCase().startsWith("host-and-port")){
				String value = bundle.getString(key);
				String[] hostPort = value.split(":");
				if (hostPort.length == 2){
					jedisClusterNodes.add(new HostAndPort(hostPort[0],new Integer(hostPort[1])));
					System.out.println("Read Cluster Node: "+value);
				}
			}
		}

		REDIS_PASSOWORD = bundle.getString("redis-password");
		REDIS_CONNECTION_TIMEOUT = new Integer(bundle.getString("redis-connection-timeout")).intValue();//int
		POOL_SET_BLOCK_WHEN_EXHAUSTED = new Boolean(bundle.getString("pool-set-block-when-exhausted"));//bool
		POOL_SET_MAX_TOTAL = new Integer(bundle.getString("pool-set-max-total")).intValue();//int
		POOL_SET_TEST_ON_BORROW = new Boolean(bundle.getString("pool-set-test-on-borrow"));//bool
		POOL_SET_TEST_ON_RETURN = new Boolean(bundle.getString("pool-set-test-on-return"));//bool
		POOL_SET_MAX_IDLE = new Integer(bundle.getString("pool-set-max-idle")).intValue();//int
		POOL_SET_MIN_IDLE = new Integer(bundle.getString("pool-set-min-idle")).intValue();//int
		POOL_SET_TEST_WHILE_IDLE = new Boolean(bundle.getString("pool-set-test-while-idle"));//bool
		POOL_SET_NUM_TESTS_PER_EVICTION_RUN = new Integer(bundle.getString("pool-set-num-tests-per-eviction-run")).intValue();//int
		POOL_SET_TIME_BETWEEN_EVICTION_RUNS_MILLIS = new Integer(bundle.getString("pool-set-time-between-eviction-runs-millis")).intValue();//int
		/*Property Loading for Inmemdeb end*/	

	}
}
