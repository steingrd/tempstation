package com.github.steingrd.fermonitor.brews;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.http.HttpServerRequest;

import com.github.steingrd.fermonitor.app.ThrowItAwayHandler;
import com.tempodb.client.Client;
import com.tempodb.models.Series;

public class CreateBrew implements ThrowItAwayHandler<HttpServerRequest> {

	final Logger log = LoggerFactory.getLogger(CreateBrew.class);
	
	final Client tempodb;

	public CreateBrew(Client tempodb) {
		this.tempodb = tempodb;
	}
	
	@Override
	public void handleAndThrow(HttpServerRequest request) throws Exception {
		String brewId = request.params().get("brewId");
		
		List<Series> existingSeries = tempodb.getSeries();
		boolean alreadyExists = existingSeries.stream().anyMatch(s -> s.getKey().equals(brewId));
		
		if (alreadyExists) {
			log.info("Series with key {} already exists, aborting.", brewId);
		} else {
			tempodb.createSeries(brewId);
			log.info("Created series with key {}", brewId);
		}
		
		request.response().setStatusCode(202).end();
	}

}
