package com.github.steingrd.fermonitor.commands;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.github.mrcritical.ironcache.IronCache;
import com.github.steingrd.fermonitor.app.IronCacheFactory;
import com.github.steingrd.fermonitor.app.JedisPoolFactory;
import com.github.steingrd.fermonitor.app.TempoDbFactory;
import com.github.steingrd.fermonitor.security.AuthorizationService;
import com.tempodb.client.Client;

import static com.github.steingrd.fermonitor.app.Configuration.get;

public class CreateBrew {

	final static Logger log = LoggerFactory.getLogger(CreateBrew.class);

	public static void main(String[] args) {
		try {
			createBrew(args);
		} catch (Exception e) {
			log.error("createBrew failed!", e);
		}
	}
	
	static void createBrew(String[] args) throws Exception {
		if (args.length != 1) {
			log.info("Missing args: " + args.length);
			return;
		}
		
		final String brewsSet = get("FERMONITOR_BREWS_LIST");
		final String brewId = args[0];
		
		final IronCache ironCache = new IronCacheFactory().create();
		final Client tempodb = new TempoDbFactory().create();
		
		try (Jedis jedis = new JedisPoolFactory().create().getResource()) {
			
			Set<String> brews = jedis.smembers(brewsSet);
			if (brews.contains(brewId)) {
				log.info("Series with key {} already exists, aborting.", brewId);
			} else {
				tempodb.createSeries(brewId);
				jedis.sadd(brewsSet, brewId);
				jedis.set(brewId + ".lastUpdated", "<>");
				log.info("Created brew with key {}", brewId);
				
				String secret = new AuthorizationService(ironCache).createSecret(brewId);
				log.info("Secret for brew {} is {} ", brewId, secret);
			}
			
		}
		
	}

}