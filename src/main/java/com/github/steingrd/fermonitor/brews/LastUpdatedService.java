package com.github.steingrd.fermonitor.brews;

import java.io.IOException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mrcritical.ironcache.IronCache;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

public class LastUpdatedService {

	final Logger log = LoggerFactory.getLogger(LastUpdatedService.class);
	
	final IronCache ironCache;
	final String cacheKey;

	public LastUpdatedService(IronCache ironCache) {
		this.ironCache = ironCache;
		this.cacheKey = fermonitorTimestampItem();
	}

	public void updatedSuccessfully() {
		try {
			this.ironCache.put(cacheKey, DateTime.now().toString());
		} catch (IOException e) {
			log.error("Failed to update Last Updated cache.", e);
		}
	}
	
	private static String fermonitorTimestampItem() {
		return propertyOrEnvVariable("FERMONITOR_TIMESTAMP_ITEM");
	}

}
