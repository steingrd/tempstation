package com.github.steingrd.fermonitor.app;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariableWithDefault;

public class FeatureToggle {

	private Boolean updateLastUpdatedTimestamp = null;
	
	public boolean shouldUpdateLastUpdatedTimestamp() {
		if (updateLastUpdatedTimestamp == null) {
			updateLastUpdatedTimestamp = Boolean.valueOf(propertyOrEnvVariableWithDefault("FEATURE_LAST_UPDATED", "false"));
		}
		
		return updateLastUpdatedTimestamp.booleanValue();
	}

}
