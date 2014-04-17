package com.github.steingrd.fermonitor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {

	static final Logger log = LoggerFactory.getLogger(Configuration.class);
	
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
