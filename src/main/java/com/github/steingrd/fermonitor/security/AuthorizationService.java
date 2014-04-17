package com.github.steingrd.fermonitor.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class AuthorizationService {

	final Logger log = LoggerFactory.getLogger(AuthorizationService.class);
	
	final JedisPool jedisPool;
	
	public AuthorizationService(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public String createSecret(String brewId) throws IOException {
		String secret = randomSecret();
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(brewId + ".secret", secret);
		}
		return secret;
	}

	public boolean isAuthorized(String brewId, String secret) {
		try (Jedis jedis = jedisPool.getResource()) {
			String cachedSecret = jedis.get(brewId + ".secret");
			
			log.debug("Authorizing key [{}] with secret [{}] against stored secret [{}]",
					brewId, secret, cachedSecret);
			
			return secret.equals(cachedSecret);
		}
		
	}
	
	private String randomSecret() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 16);
	}

}
