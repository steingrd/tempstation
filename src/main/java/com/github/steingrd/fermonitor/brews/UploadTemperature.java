package com.github.steingrd.fermonitor.brews;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;

import redis.clients.jedis.JedisPool;

import com.github.steingrd.fermonitor.app.FeatureToggle;
import com.github.steingrd.fermonitor.app.ThrowItAwayHandler;
import com.tempodb.client.Client;
import com.tempodb.models.DataPoint;

import static com.google.common.collect.Lists.newArrayList;

public class UploadTemperature implements ThrowItAwayHandler<HttpServerRequest> {

	final Logger log = LoggerFactory.getLogger(UploadTemperature.class);
	
	final FeatureToggle featureToggle;
	final Client tempodb;
	final LastUpdatedService lastUpdated;
	final Vertx vertx;
	
	public UploadTemperature(Vertx vertx, Client tempodb, JedisPool jedis) {
		this.vertx = vertx;
		this.tempodb = tempodb;
		this.lastUpdated = new LastUpdatedService(jedis);
		this.featureToggle = new FeatureToggle();
	}

	@Override
	public void handleAndThrow(HttpServerRequest request) {
		String brewId = request.params().get("brewId");
		DateTime timestamp = DateTime.parse(request.params().get("timestamp"));
		double temperature = Double.parseDouble(request.params().get("temperature"));
		
		if (featureToggle.tempoDbEnabled()) {
			vertx.runOnContext(event -> {
				try {
					tempodb.writeKey(brewId, newArrayList(new DataPoint(timestamp, temperature)));
					log.info("Saved timestamp {} and temperature to tempodb {}.", timestamp, temperature);
				} catch (Exception e) {
					log.error("Failed to update tempodb", e);
					request.response().setStatusCode(500).end(e.getMessage());
					return;
				}
				log.debug("Successfully updated tempodb");
			});
		}
		
		if (featureToggle.shouldUpdateLastUpdatedTimestamp()) {
			vertx.runOnContext(event -> {
				lastUpdated.updatedSuccessfully(brewId);
				log.info("Successfully updated redis with latest timestamp for {}", brewId);
			});
		}

		request.response().end();
	}

}
