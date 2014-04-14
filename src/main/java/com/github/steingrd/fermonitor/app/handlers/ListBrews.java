package com.github.steingrd.fermonitor.app.handlers;

import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import com.github.steingrd.fermonitor.app.ThrowItAwayHandler;
import com.tempodb.client.Client;

public class ListBrews implements ThrowItAwayHandler<HttpServerRequest> {

	final Client tempodb;

	public ListBrews(Client tempodb) {
		this.tempodb = tempodb;
	}

	@Override
	public void handleAndThrow(HttpServerRequest request) throws Exception {
		Buffer responseBuffer = new Buffer();
		
		tempodb.getSeries().forEach(s -> {
			responseBuffer.appendString(s.getKey() + "\n");
		});
		
		request.response().end(responseBuffer);
	}

}
