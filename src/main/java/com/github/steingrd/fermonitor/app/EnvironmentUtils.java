package com.github.steingrd.fermonitor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentUtils {

	static final Logger log = LoggerFactory.getLogger(EnvironmentUtils.class);
	
	public static String propertyOrEnvVariable(String name) {
		String value = propertyOrEnvVariableWithDefault(name, null);
		if (value == null) {
			throw new RuntimeException("No environment variable or system property named " + name);
		}
		return value;
	}

	public static String propertyOrEnvVariableWithDefault(String name, String defaultValue) {
		String value = System.getProperty(name);
		if (value != null) {
			log.info("System property [{}] is [{}].", name, value);
			return value;
		}

		value = System.getenv(name);
		if (value != null) {
			log.info("Environment variable [{}] is [{}].", name, value);
			return value;
		}

		log.info("No environment variable or system property with name [{}], using default value [{}].", name, defaultValue);

		return defaultValue;
	}
	
}
