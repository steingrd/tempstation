package com.github.steingrd.fermonitor.app.handlers;

import org.joda.time.DateTime;
import org.vertx.java.core.http.HttpServerRequest;

import com.github.steingrd.fermonitor.app.ThrowItAwayHandler;
import com.tempodb.client.Client;
import com.tempodb.models.DataPoint;

import static com.google.common.collect.Lists.newArrayList;

public class UploadTemperature implements ThrowItAwayHandler<HttpServerRequest> {

	final Client tempodb;
	
	public UploadTemperature(Client tempodb) {
		this.tempodb = tempodb;
	}

	@Override
	public void handleAndThrow(HttpServerRequest request) throws Exception{
		String brewId = request.params().get("brewId");
		DateTime timestamp = DateTime.parse(request.params().get("timestamp"));
		double temperature = Double.parseDouble(request.params().get("temperature"));
		
		tempodb.writeKey(brewId, newArrayList(new DataPoint(timestamp, temperature)));
	}

}
