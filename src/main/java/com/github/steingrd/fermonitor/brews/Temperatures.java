package com.github.steingrd.fermonitor.brews;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import redis.clients.jedis.JedisPool;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tempodb.client.Client;
import com.tempodb.models.DataSet;
import com.tempodb.models.Filter;

import static java.util.stream.Collectors.toList;

public class Temperatures implements Handler<HttpServerRequest> {

	final Logger log = LoggerFactory.getLogger(Temperatures.class);
	
	final JedisPool jedisPool;
	final Client tempodb;

	public Temperatures(Client tempodb, JedisPool jedisPool) {
		this.tempodb = tempodb;
		this.jedisPool = jedisPool;
	}

	@Override
	public void handle(HttpServerRequest request) {
		
		log.info("Params: " + request.params());
		
		final String brewId = request.params().get("brewId");
		final String interval = "" + (Integer.parseInt(request.params().get("step")) / 60.000)  + "min";
		final DateTime start = DateTime.parse(request.params().get("start"));
		final DateTime stop = DateTime.parse(request.params().get("stop"));
		
		final Filter filter = new Filter();
		filter.addKey(brewId);
		
		final String function = "mean";
		
		DataSet dataSet = null;
		try {
			dataSet = tempodb.read(start, stop, filter, interval, function).get(0);
		} catch (Exception e) {
			log.error("Could not fetch datasets from TempoDb");
			request.response().setStatusCode(500).end();
			return;
		}
		
		List<DataInfo> converted = dataSet.getData().stream().map(ds -> {
			return new DataInfo(ds.getTimestamp().toString(), ds.getValue(), ds.getValue());
		}).collect(toList());
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json;
		try {
			json = objectMapper.writeValueAsString(converted);
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize datasets as json");
			request.response().setStatusCode(500).end();
			return;
		}
		
		request.response().putHeader("Content-Type", "application/json").end(json);
	}
	
	static class DataInfo {

		@JsonProperty String ts;
		@JsonProperty Number value;
		@JsonProperty Number temperature;

		public DataInfo(String ts, Number value, Number temperature) {
			this.ts = ts;
			this.value = value;
			this.temperature = temperature;
		}
		
	}

}
