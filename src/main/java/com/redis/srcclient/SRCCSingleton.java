package com.redis.srcclient;

/*
 * Author prash_mi@yahoo.com
 * 
 * This class is singleton thread safe implementation for initializing pooled connection
 * 
 */

public class SRCCSingleton {
	private static volatile InMemoryDSConnection inMemoryDSConnection;
	private static volatile Object lock = new Object();

	private SRCCSingleton(){}

	public static InMemoryDSConnection getInstance(){//Thread safe singleton initialization
		if(inMemoryDSConnection == null){
			synchronized(lock){//thread safe initialization
				if(inMemoryDSConnection!= null){//checking again to make certain that only one instance would be spawned, as more than one threads might enter the previous null check 
					return inMemoryDSConnection;
				}
				inMemoryDSConnection = new SRCConnection();
			}
		}
		return inMemoryDSConnection;
	}


}
