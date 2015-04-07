## What is simple-redis-cluster-client (SRCC)?
SRCC is a Java API on top of Jedis 2.6 which shares keys between Redis 2.8 hosts running in stand alone mode. It allows Redis 2.8 servers which doesn't support clustering to horizontally scale.

## How to Use It?
1> Start Redis 2.8 nodes in stand alone mode (You can use configuration file @ srcc/release/redisNonCluster.conf).

2> Enter the Redis Hosts and ports details in srcc_en_US.properties which is found at src/main/resources and Build the project.

3> Simply consome SRCC using a Java code like:

```java
InMemoryDSConnection inMemCon = SRCCSingleton.getInstance();//Get Instance and initialize cluster connection
inMemCon.putInCache("TestKey", "TestValue");//put a value in cluster
System.out.println("TestKey Set");//
System.out.println("Retrieved from cache: "+inMemCon.getFromCache("TestKey"));//get the value back from the cluster
```

## License
Open source under Apache License Version 2.0
