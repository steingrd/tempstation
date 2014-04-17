package com.github.steingrd.fermonitor.app;

import com.github.mrcritical.ironcache.DefaultIronCache;
import com.github.mrcritical.ironcache.IronCache;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

public class IronCacheFactory {

	public IronCache create() {
		return new DefaultIronCache(ironCacheToken(), ironCacheProjectId(), ironCacheCacheName());
	}

	private static String ironCacheProjectId() {
		return propertyOrEnvVariable("IRON_CACHE_PROJECT_ID");
	}

	private static String ironCacheToken() {
		return propertyOrEnvVariable("IRON_CACHE_TOKEN");
	}
	
	private static String ironCacheCacheName() {
		return propertyOrEnvVariable("IRON_CACHE_CACHE_NAME");
	}
	
}
