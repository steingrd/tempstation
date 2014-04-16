package com.github.steingrd.fermonitor.commands;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mrcritical.ironcache.DefaultIronCache;
import com.github.mrcritical.ironcache.IronCache;
import com.github.steingrd.fermonitor.security.AuthorizationService;

public class CreateSecret {

	static final Logger log = LoggerFactory.getLogger(CreateSecret.class);
	
	public static void main(String[] args) {
		try {
			createSecret();
		} catch (Exception e) {
			log.error("CreateSecret failed!", e);
		}
	}
	
	public static void createSecret() {
		if (args.length != 1) {
			log.info("Missing args: " + args.length);
			return;
		}
		
		IronCache ironCache = new DefaultIronCache(ironCacheToken(), ironCacheProjectId(), ironCacheCacheName());
		AuthorizationService authRepo = new AuthorizationService(ironCache);
		
		String key = authRepo.createSecret(args[0]);
		log.info("Created key: {} ", key);
	}

	private static String ironCacheCacheName() {
		return propertyOrEnvVariable("IRON_CACHE_CACHE_NAME");
	}
	
	private static String ironCacheProjectId() {
		return propertyOrEnvVariable("IRON_CACHE_PROJECT_ID");
	}

	private static String ironCacheToken() {
		return propertyOrEnvVariable("IRON_CACHE_TOKEN");
	}
}
