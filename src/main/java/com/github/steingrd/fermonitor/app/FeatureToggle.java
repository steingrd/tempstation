package com.github.steingrd.fermonitor.app;

import static com.github.steingrd.fermonitor.app.Configuration.get;

public class FeatureToggle {

	private Boolean updateLastUpdatedTimestamp = null;
	private Boolean enableTempoDb = null;
	private Boolean enableIronCache = null; 
	private Boolean enableRedisCloud = null;
	private Boolean verifyUploadsEnabled = null;
	private Boolean useClasspathForStaticResources = null;
	private Boolean protectedHandlerEnabled = null;
	
	public boolean protectedHandlerEnabled() {
		if (protectedHandlerEnabled == null) {
			protectedHandlerEnabled = Boolean.valueOf(get("FEATURE_PROTECTED_HANDLER", "true"));
		}

		return protectedHandlerEnabled;
	}

	public boolean shouldUpdateLastUpdatedTimestamp() {
		if (updateLastUpdatedTimestamp == null) {
			updateLastUpdatedTimestamp = Boolean.valueOf(get("FEATURE_LAST_UPDATED", "false"));
		}
		
		return updateLastUpdatedTimestamp;
	}

	public boolean tempoDbEnabled() {
		if (enableTempoDb == null) {
			enableTempoDb = Boolean.valueOf(get("FEATURE_TEMPO_DB", "true"));
		}
		
		return enableTempoDb;
	}
	
	public boolean ironCacheEnabled() {
		if (enableIronCache == null) {
			enableIronCache = Boolean.valueOf(get("FEATURE_IRON_CACHE", "true"));
		}
		
		return enableIronCache;
	}
	
	public boolean redisCloudEnabled() {
		if (enableRedisCloud == null) {
			enableRedisCloud = Boolean.valueOf(get("FEATURE_REDIS_CLOUD", "true"));
		}
		
		return enableRedisCloud;
	}

	public boolean verifyUploadsEnabled() {
		if (verifyUploadsEnabled == null) {
			verifyUploadsEnabled = Boolean.valueOf(get("FEATURE_VERIFY_UPLOADS", "true"));
		}
		
		return verifyUploadsEnabled;
	}

	public boolean useClasspathForStaticResources() {
		if (useClasspathForStaticResources == null) {
			useClasspathForStaticResources = Boolean.valueOf(get("FEATURE_RESOURCES_FROM_CLASSPATH", "true"));
		}
		
		return useClasspathForStaticResources;
	}

}
