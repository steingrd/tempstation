package com.github.steingrd.fermonitor.brews;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.http.HttpServerRequest;

import com.github.mrcritical.ironcache.IronCache;
import com.github.steingrd.fermonitor.app.ThrowItAwayHandler;
import com.tempodb.client.Client;
import com.tempodb.models.DataPoint;

import static com.google.common.collect.Lists.newArrayList;

public class UploadTemperature implements ThrowItAwayHandler<HttpServerRequest> {

	final Logger log = LoggerFactory.getLogger(UploadTemperature.class);
	
	final Client tempodb;
	final LastUpdatedService lastUpdated;
	
	public UploadTemperature(Client tempodb, IronCache ironCache) {
		this.tempodb = tempodb;
		this.lastUpdated = new LastUpdatedService(ironCache);
	}

	@Override
	public void handleAndThrow(HttpServerRequest request) {
		String brewId = request.params().get("brewId");
		DateTime timestamp = DateTime.parse(request.params().get("timestamp"));
		double temperature = Double.parseDouble(request.params().get("temperature"));
		
		log.debug("Writing timestamp {} and temperature to tempodb {}.", timestamp, temperature);
		
		try {
			tempodb.writeKey(brewId, newArrayList(new DataPoint(timestamp, temperature)));
		} catch (Exception e) {
			log.error("Failed to update tempodb", e);
			request.response().setStatusCode(500).end(e.getMessage());
			return;
		}
		
		log.debug("Successfully updated tempodb");
		lastUpdated.updatedSuccessfully();
		request.response().end();
	}

}
