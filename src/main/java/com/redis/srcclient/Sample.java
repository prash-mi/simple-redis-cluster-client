package com.redis.srcclient;

/*
 * @author prash_mi@yahoo.com
 * 
 * This class simply tests connection by setting and getting a key
 * 
 * 
 */

public class Sample {

	public static void main(String[] args) {

		InMemoryDSConnection inMemCon = SRCCSingleton.getInstance();//Get Instance and initialize cluster connection
		inMemCon.putInCache("TestKey", "TestValue");//put a value in cluster
		System.out.println("TestKey Set");//
		System.out.println("Retrieved from cache: "+inMemCon.getFromCache("TestKey"));//get the value back from the cluster

	}

}
