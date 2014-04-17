package com.github.steingrd.fermonitor.brews;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import redis.clients.jedis.JedisPool;

import com.tempodb.client.Client;

public class Temperatures implements Handler<HttpServerRequest> {

	final JedisPool jedisPool;
	final Client tempodb;

	public Temperatures(Client tempodb, JedisPool jedisPool) {
		this.tempodb = tempodb;
		this.jedisPool = jedisPool;
	}

	@Override
	public void handle(HttpServerRequest event) {
	}

}
