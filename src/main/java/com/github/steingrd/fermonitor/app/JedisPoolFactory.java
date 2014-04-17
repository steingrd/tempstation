package com.github.steingrd.fermonitor.app;

import java.net.URI;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

public class JedisPoolFactory {

	public JedisPool create() {
		try {
			URI uri = new URI(propertyOrEnvVariable("REDISCLOUD_URL"));
			
			return new JedisPool(
				new JedisPoolConfig(), 
				uri.getHost(), 
				uri.getPort(),
				Protocol.DEFAULT_TIMEOUT,
				uri.getUserInfo().split(":",2)[1]);
			
		} catch (Exception e) {
			throw new RuntimeException("Could not create JedisPool", e);
		}
	}

}
