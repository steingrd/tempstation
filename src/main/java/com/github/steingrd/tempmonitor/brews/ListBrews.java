package com.github.steingrd.tempmonitor.brews;

import java.util.LinkedList;
import java.util.List;

import org.vertx.java.core.http.HttpServerRequest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.steingrd.tempmonitor.app.ThrowItAwayHandler;

import static com.github.steingrd.tempmonitor.app.Configuration.get;

public class ListBrews implements ThrowItAwayHandler<HttpServerRequest> {

	final JedisPool jedisPool;
	final String brewsSetKey;

	public ListBrews(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
		this.brewsSetKey = get("APP_BREWS_LIST");
		
	}

	@Override
	public void handleAndThrow(HttpServerRequest request) throws Exception {
		
		final List<BrewInfo> result = new LinkedList<>();

		try (Jedis jedis = jedisPool.getResource()) {
			for (String brew : jedis.smembers(brewsSetKey)) {
				final String lastUpdated = jedis.get(brew + ".lastUpdated");
				result.add(new BrewInfo(brew, lastUpdated));
			}
			jedisPool.returnResource(jedis);
		}
		

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(result);
		request.response().putHeader("Content-Type", "application/json").end(json);
		
	}
	
	private static class BrewInfo {
		
		@JsonProperty String key;
		@JsonProperty String lastUpdated;

		public BrewInfo(String key, String lastUpdated) {
			this.key = key;
			this.lastUpdated = lastUpdated;
		}
	}

}
