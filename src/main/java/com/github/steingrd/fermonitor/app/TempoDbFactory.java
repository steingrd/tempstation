package com.github.steingrd.fermonitor.app;

import com.tempodb.client.Client;
import com.tempodb.client.ClientBuilder;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

public class TempoDbFactory {

	public Client create() {
		if (new FeatureToggle().tempoDbEnabled()) {
			return new ClientBuilder()
				.host(tempodbHost())
				.port(tempodbPort())
				.key(tempodbKey())
				.secret(tempodbSecret())
				.secure(tempodbSecure())
				.build();
		} else {
			return null;
		}
	}

	private static boolean tempodbSecure() {
		return Boolean.parseBoolean(propertyOrEnvVariable("TEMPODB_API_SECURE").toLowerCase());
	}

	private static String tempodbSecret() {
		return propertyOrEnvVariable("TEMPODB_API_SECRET");
	}

	private static String tempodbKey() {
		return propertyOrEnvVariable("TEMPODB_API_KEY");
	}

	private static int tempodbPort() {
		return Integer.parseInt(propertyOrEnvVariable("TEMPODB_API_PORT"));
	}
	
	private static String tempodbHost() {
		return propertyOrEnvVariable("TEMPODB_API_HOST");
	}
	
}
