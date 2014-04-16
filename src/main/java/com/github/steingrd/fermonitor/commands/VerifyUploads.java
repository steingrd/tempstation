package com.github.steingrd.fermonitor.commands;

import java.io.IOException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mrcritical.ironcache.CacheItem;
import com.github.mrcritical.ironcache.DefaultIronCache;
import com.github.mrcritical.ironcache.IronCache;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;

public class VerifyUploads {

	static final Logger log = LoggerFactory.getLogger(VerifyUploads.class);
	
	public static void main(String[] args) {
		try {
			verifyUploads();
		} catch (Exception e) {
			log.error("VerifyUploads failed!", e);
		}
	}

	public static void verifyUploads() {
		IronCache ironCache = new DefaultIronCache(ironCacheToken(), ironCacheProjectId(), ironCacheCacheName());

		CacheItem cachedTimestamp;
		try {
			cachedTimestamp = ironCache.get(fermonitorTimestampItem());
		} catch (IOException e) {
			log.error("Failed to retrieve cache item", e);
			return;
		}

		DateTime lastUpdated = DateTime.parse(cachedTimestamp.getValue());
		if (lastUpdated.plusMinutes(5).isBeforeNow()) {
			log.warn("Last update to database was on {}", lastUpdated);
		}
	}

	private static String fermonitorTimestampItem() {
		return propertyOrEnvVariable("FERMONITOR_TIMESTAMP_ITEM");
	}
	
	private static String ironCacheCacheName() {
		return propertyOrEnvVariable("IRON_CACHE_CACHE_NAME");
	}
	
	private static String ironCacheProjectId() {
		return propertyOrEnvVariable("IRON_CACHE_PROJECT_ID");
	}

	private static String ironCacheToken() {
		return propertyOrEnvVariable("IRON_CACHE_TOKEN");
	}
}
