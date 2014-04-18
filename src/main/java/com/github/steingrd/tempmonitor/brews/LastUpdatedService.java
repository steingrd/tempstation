package com.github.steingrd.tempmonitor.brews;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class LastUpdatedService {

	final Logger log = LoggerFactory.getLogger(LastUpdatedService.class);
	
	final JedisPool jedisPool;

	public LastUpdatedService(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void updatedSuccessfully(String brewId) {
		Jedis jedis = null;
		
		try {
			jedis = jedisPool.getResource();
			jedis.set(brewId + ".lastUpdated", DateTime.now().toString());
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}
	
}
