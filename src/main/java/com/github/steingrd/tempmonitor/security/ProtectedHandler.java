package com.github.steingrd.tempmonitor.security;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import redis.clients.jedis.JedisPool;

import com.github.steingrd.tempmonitor.app.FeatureToggle;

public class ProtectedHandler implements Handler<HttpServerRequest> {

	final Handler<HttpServerRequest> handler;
	final AuthorizationService authService;
	final FeatureToggle featureToggle;

	public ProtectedHandler(JedisPool jedisPool, Handler<HttpServerRequest> handler) {
		this.handler = handler;
		this.featureToggle = new FeatureToggle();
		this.authService = new AuthorizationService(jedisPool);
	}

	@Override
	public void handle(HttpServerRequest request) {
		if (featureToggle.protectedHandlerEnabled()) {
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
		}
		
		handler.handle(request);
	}

}
