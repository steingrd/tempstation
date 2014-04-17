package com.github.steingrd.fermonitor.commands;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.github.steingrd.fermonitor.app.JedisPoolFactory;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

public class VerifyUploads {

	static final Logger log = LoggerFactory.getLogger(VerifyUploads.class);
	
	public static void main(String[] args) {
		try {
			verifyUploads();
		} catch (Exception e) {
			log.error("VerifyUploads failed!", e);
		}
	}

	public static void verifyUploads() throws Exception {
		
		String cacheKey = fermonitorTimestampItem();
		JedisPool jedisPool = new JedisPoolFactory().create();
		
		Jedis jedis = null;
		String cachedTimestamp = null;
		
		try {
			jedis = jedisPool.getResource();
			cachedTimestamp = jedis.get(cacheKey);
			
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		
		if (cachedTimestamp == null) {
			log.warn("{} not found in redis instance, could not verify last update time", cacheKey);
		} else {
			DateTime lastUpdated = DateTime.parse(cachedTimestamp);
			if (lastUpdated.plusMinutes(5).isBeforeNow()) {
				log.warn("Last update to database was on {}", lastUpdated);
			}
		}
	}

	private static String fermonitorTimestampItem() {
		return propertyOrEnvVariable("FERMONITOR_TIMESTAMP_ITEM");
	}
	
	
	
}
