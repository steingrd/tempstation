package com.github.steingrd.fermonitor.brews;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

public class LastUpdatedService {

	final Logger log = LoggerFactory.getLogger(LastUpdatedService.class);
	
	final String cacheKey;

	final JedisPool jedisPool;

	public LastUpdatedService(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
		this.cacheKey = fermonitorTimestampItem();
	}

	public void updatedSuccessfully() {
		Jedis jedis = null;
		
		try {
			jedis = jedisPool.getResource();
			jedis.set(cacheKey, DateTime.now().toString());
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}
	
	private static String fermonitorTimestampItem() {
		return propertyOrEnvVariable("FERMONITOR_TIMESTAMP_ITEM");
	}

}
