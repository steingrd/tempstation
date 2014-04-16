package com.github.steingrd.fermonitor.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mrcritical.ironcache.CacheItem;
import com.github.mrcritical.ironcache.IronCache;

public class AuthorizationService {

	final Logger log = LoggerFactory.getLogger(AuthorizationService.class);
	
	final IronCache ironCache;

	public AuthorizationService(IronCache ironCache) {
		this.ironCache = ironCache;
	}

	public String createSecret(String key) throws IOException {
		String secret = randomSecret();
		ironCache.put(key, secret);
		return secret;
	}

	public boolean isAuthorized(String key, String secret) {
		CacheItem cachedSecret;
		try {
			cachedSecret = ironCache.get(key);
		} catch (IOException e) {
			log.error("Failed to retrieve cache item", e);
			return false;
		}
		
		log.debug("Authorizing key [{}] with secret [{}] against stored secret [{}]",
			key, secret, cachedSecret);
		
		return secret.equals(cachedSecret);
	}
	
	private String randomSecret() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 16);
	}

}
