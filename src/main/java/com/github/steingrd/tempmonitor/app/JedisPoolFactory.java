package com.github.steingrd.tempmonitor.app;

import java.net.URI;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import static com.github.steingrd.tempmonitor.app.Configuration.get;

public class JedisPoolFactory {

	public JedisPool create() {
		if (new FeatureToggle().redisCloudEnabled()) {
			try {
				URI uri = new URI(get("REDISCLOUD_URL"));
				
				return new JedisPool(
					new JedisPoolConfig(), 
					uri.getHost(), 
					uri.getPort(),
					Protocol.DEFAULT_TIMEOUT,
					uri.getUserInfo().split(":",2)[1]);
				
			} catch (Exception e) {
				throw new RuntimeException("Could not create JedisPool", e);
			}
		} else {
			return null;
		}
	}

}
