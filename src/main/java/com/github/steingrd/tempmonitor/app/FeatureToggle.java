package com.github.steingrd.tempmonitor.app;

import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_GRAPHITE;
import static com.github.steingrd.tempmonitor.app.Configuration.FEATURE_INFLUXDB;
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
	private Boolean graphiteEnabled = null;
	private Boolean influxdbEnabled = null;
	
	public boolean influxdbEnabled() {
		if (influxdbEnabled == null) {
			influxdbEnabled = get(FEATURE_INFLUXDB, true);
		}
		
		return influxdbEnabled;
	}
	
	public boolean protectedHandlerEnabled() {
		if (protectedHandlerEnabled == null) {
			protectedHandlerEnabled = get(FEATURE_PROTECTED_HANDLER, true);
		}

		return protectedHandlerEnabled;
	}

	public boolean shouldUpdateLastUpdatedTimestamp() {
		if (updateLastUpdatedTimestamp == null) {
			updateLastUpdatedTimestamp = get(FEATURE_LAST_UPDATED, false);
		}
		
		return updateLastUpdatedTimestamp;
	}

	public boolean tempoDbEnabled() {
		if (enableTempoDb == null) {
			enableTempoDb = get(FEATURE_TEMPO_DB, true);
		}
		
		return enableTempoDb;
	}
	
	public boolean redisCloudEnabled() {
		if (enableRedisCloud == null) {
			enableRedisCloud = get(FEATURE_REDIS_CLOUD, true);
		}
		
		return enableRedisCloud;
	}

	public boolean verifyUploadsEnabled() {
		if (verifyUploadsEnabled == null) {
			verifyUploadsEnabled = get(FEATURE_VERIFY_UPLOADS, true);
		}
		
		return verifyUploadsEnabled;
	}

	public boolean useClasspathForStaticResources() {
		if (useClasspathForStaticResources == null) {
			useClasspathForStaticResources = get(FEATURE_RESOURCES_FROM_CLASSPATH, true);
		}
		
		return useClasspathForStaticResources;
	}

	public boolean graphiteEnabled() {
		if (graphiteEnabled == null) {
			graphiteEnabled = get(FEATURE_GRAPHITE, true);
		}
		
		return graphiteEnabled;
	}
	
}
