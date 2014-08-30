package com.github.steingrd.tempmonitor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Configuration {
	APP_VERIFY_SERIES,
	APP_BREWS_LIST,
	FEATURE_LAST_UPDATED,
	FEATURE_PROTECTED_HANDLER,
	FEATURE_REDIS_CLOUD,
	FEATURE_RESOURCES_FROM_CLASSPATH,
	FEATURE_TEMPO_DB,
	FEATURE_VERIFY_UPLOADS,
	FEATURE_GRAPHITE,
	// heroku configurations below
	REDISCLOUD_URL;

	static final Logger log = LoggerFactory.getLogger(Configuration.class);
	
	public static String get(Configuration setting) {
		return get(setting.name());
	}
	
	public static String get(Configuration setting, String defaultValue) {
		return get(setting.name(), defaultValue);
	}
	
	public static boolean get(Configuration setting, boolean defaultValue) {
		return Boolean.parseBoolean(get(setting.name(), Boolean.toString(defaultValue)));
	}
	
	public static String get(String name) {
		String value = get(name, null);
		if (value == null) {
			throw new RuntimeException("No environment variable or system property named " + name);
		}
		return value;
	}

	public static String get(String name, String defaultValue) {
		String value = System.getProperty(name);
		if (value != null) {
			log.info("[{}] is [{}].", name, value);
			return value;
		}

		value = System.getenv(name);
		if (value != null) {
			log.info("[{}] is [{}].", name, value);
			return value;
		}

		log.info("[{}] not set, using default value [{}].", name, defaultValue);

		return defaultValue;
	}
	
}
