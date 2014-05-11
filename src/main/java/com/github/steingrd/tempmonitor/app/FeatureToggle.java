package com.github.steingrd.tempmonitor.app;

import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_LAST_UPDATED;
import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_PROTECTED_HANDLER;
import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_REDIS_CLOUD;
import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_RESOURCES_FROM_CLASSPATH;
import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_TEMPO_DB;
import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_VERIFY_UPLOADS;
import static com.github.steingrd.tempmonitor.app.Configuration.get;

public class FeatureToggle {

	private Boolean updateLastUpdatedTimestamp = null;
	private Boolean enableTempoDb = null;
	private Boolean enableRedisCloud = null;
	private Boolean verifyUploadsEnabled = null;
	private Boolean useClasspathForStaticResources = null;
	private Boolean protectedHandlerEnabled = null;
	
	public boolean protectedHandlerEnabled() {
		if (protectedHandlerEnabled == null) {
			protectedHandlerEnabled = Boolean.valueOf(get(FEATURE_PROTECTED_HANDLER, "true"));
		}

		return protectedHandlerEnabled;
	}

	public boolean shouldUpdateLastUpdatedTimestamp() {
		if (updateLastUpdatedTimestamp == null) {
			updateLastUpdatedTimestamp = Boolean.valueOf(get(FEATURE_LAST_UPDATED, "false"));
		}
		
		return updateLastUpdatedTimestamp;
	}

	public boolean tempoDbEnabled() {
		if (enableTempoDb == null) {
			enableTempoDb = Boolean.valueOf(get(FEATURE_TEMPO_DB, "true"));
		}
		
		return enableTempoDb;
	}
	
	public boolean redisCloudEnabled() {
		if (enableRedisCloud == null) {
			enableRedisCloud = Boolean.valueOf(get(FEATURE_REDIS_CLOUD, "true"));
		}
		
		return enableRedisCloud;
	}

	public boolean verifyUploadsEnabled() {
		if (verifyUploadsEnabled == null) {
			verifyUploadsEnabled = Boolean.valueOf(get(FEATURE_VERIFY_UPLOADS, "true"));
		}
		
		return verifyUploadsEnabled;
	}

	public boolean useClasspathForStaticResources() {
		if (useClasspathForStaticResources == null) {
			useClasspathForStaticResources = Boolean.valueOf(get(FEATURE_RESOURCES_FROM_CLASSPATH, "true"));
		}
		
		return useClasspathForStaticResources;
	}

}
