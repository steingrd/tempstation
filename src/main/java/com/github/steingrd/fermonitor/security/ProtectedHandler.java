package com.github.steingrd.fermonitor.security;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import com.github.mrcritical.ironcache.IronCache;

public class ProtectedHandler implements Handler<HttpServerRequest> {

	final Handler<HttpServerRequest> handler;
	final AuthorizationService authService;

	public ProtectedHandler(IronCache ironCache, Handler<HttpServerRequest> handler) {
		this.handler = handler;
		this.authService = new AuthorizationService(ironCache);
	}

	@Override
	public void handle(HttpServerRequest request) {
		String key = request.headers().get("X-Fermonitor-Key");
		if (key == null) {
			request.response().setStatusCode(400).end("Missing header X-Fermonitor-Key");
			return;
		}
		
		String secret = request.headers().get("X-Fermonitor-Secret");
		if (secret == null) {
			request.response().setStatusCode(400).end("Missing header X-Fermonitor-Secret");
			return;
		}
		
		if (!authService.isAuthorized(key, secret)) {
			request.response().setStatusCode(403).end();
			return;
		}
		
		handler.handle(request);
	}

}
