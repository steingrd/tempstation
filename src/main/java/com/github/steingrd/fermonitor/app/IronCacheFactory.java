package com.github.steingrd.fermonitor.app;

import com.github.mrcritical.ironcache.DefaultIronCache;
import com.github.mrcritical.ironcache.IronCache;

import static com.github.steingrd.fermonitor.app.Configuration.get;

public class IronCacheFactory {

	public IronCache create() {
		if (new FeatureToggle().ironCacheEnabled()) {
			return new DefaultIronCache(ironCacheToken(), ironCacheProjectId(), ironCacheCacheName());
		} else {
			return null;
		}
	}

	private static String ironCacheProjectId() {
		return get("IRON_CACHE_PROJECT_ID");
	}

	private static String ironCacheToken() {
		return get("IRON_CACHE_TOKEN");
	}
	
	private static String ironCacheCacheName() {
		return get("IRON_CACHE_CACHE_NAME");
	}
	
}
