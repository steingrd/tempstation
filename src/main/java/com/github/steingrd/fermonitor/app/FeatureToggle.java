package com.github.steingrd.fermonitor.app;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariableWithDefault;

public class FeatureToggle {

	private Boolean updateLastUpdatedTimestamp = null;
	private Boolean enableTempoDb = null;
	private Boolean enableIronCache = null; 
	private Boolean enableRedisCloud = null;
	private Boolean verifyUploadsEnabled = null;
	
	public boolean shouldUpdateLastUpdatedTimestamp() {
		if (updateLastUpdatedTimestamp == null) {
			updateLastUpdatedTimestamp = Boolean.valueOf(propertyOrEnvVariableWithDefault("FEATURE_LAST_UPDATED", "false"));
		}
		
		return updateLastUpdatedTimestamp;
	}

	public boolean tempoDbEnabled() {
		if (enableTempoDb == null) {
			enableTempoDb = Boolean.valueOf(propertyOrEnvVariableWithDefault("FEATURE_TEMPO_DB", "true"));
		}
		
		return enableTempoDb;
	}
	
	public boolean ironCacheEnabled() {
		if (enableIronCache == null) {
			enableIronCache = Boolean.valueOf(propertyOrEnvVariableWithDefault("FEATURE_IRON_CACHE", "true"));
		}
		
		return enableIronCache;
	}
	
	public boolean redisCloudEnabled() {
		if (enableRedisCloud == null) {
			enableRedisCloud = Boolean.valueOf(propertyOrEnvVariableWithDefault("FEATURE_REDIS_CLOUD", "true"));
		}
		
		return enableRedisCloud;
	}

	public boolean verifyUploadsEnabled() {
		if (verifyUploadsEnabled == null) {
			verifyUploadsEnabled = Boolean.valueOf(propertyOrEnvVariableWithDefault("FEATURE_VERIFY_UPLOADS", "true"));
		}
		
		return verifyUploadsEnabled;
	}

}
