package com.github.steingrd.fermonitor.commands;

import java.net.URI;
import java.net.URISyntaxException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
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
		final JedisPool jedisPool = new JedisPool(
				new JedisPoolConfig(), 
				jedisHost(), 
				jedisPort(),
				Protocol.DEFAULT_TIMEOUT,
				jedisPassword());
		
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
	
	private static String jedisPassword() throws URISyntaxException {
		URI uri = new URI(propertyOrEnvVariable("REDISCLOUD_URL"));
		return uri.getUserInfo().split(":",2)[1];
	}
	
	private static int jedisPort() throws URISyntaxException {
		URI uri = new URI(propertyOrEnvVariable("REDISCLOUD_URL"));
		return uri.getPort();
	}

	private static String jedisHost() throws URISyntaxException {
		URI uri = new URI(propertyOrEnvVariable("REDISCLOUD_URL"));
		return uri.getHost();
	}
	
}
