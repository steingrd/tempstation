package com.github.steingrd.tempmonitor.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.github.steingrd.tempmonitor.app.JedisPoolFactory;
import com.github.steingrd.tempmonitor.app.TempoDbFactory;
import com.tempodb.client.Client;
import com.tempodb.models.Filter;

import static com.github.steingrd.tempmonitor.app.Configuration.get;

public class DeleteSeries {

	final static Logger log = LoggerFactory.getLogger(DeleteSeries.class);

	public static void main(String[] args) {
		try {
			deleteSeries(args);
		} catch (Exception e) {
			log.error("deleteSeries failed!", e);
		}
	}
	
	static void deleteSeries(String[] args) throws Exception {
		if (args.length != 1) {
			log.info("Missing args: " + args.length);
			return;
		}
		
		final String brewsSet = get("FERMONITOR_BREWS_LIST");
		final String brewId = args[0];
		
		final JedisPool jedisPool = new JedisPoolFactory().create();
		final Client tempodb = new TempoDbFactory().create();
		
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.srem(brewsSet, brewId);
			jedis.del(brewId + ".lastUpdated");
			jedis.del(brewId + ".secret");
			jedisPool.returnResource(jedis);			
		}
		
		Filter filter = new Filter();
		filter.addKey(brewId);
		tempodb.deleteSeries(filter);
		
	}

}