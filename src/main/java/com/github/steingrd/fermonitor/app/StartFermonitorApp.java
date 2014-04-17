package com.github.steingrd.fermonitor.app;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.github.mrcritical.ironcache.DefaultIronCache;
import com.github.mrcritical.ironcache.IronCache;
import com.github.steingrd.fermonitor.brews.CreateBrew;
import com.github.steingrd.fermonitor.brews.ListBrews;
import com.github.steingrd.fermonitor.brews.UploadTemperature;
import com.github.steingrd.fermonitor.security.ProtectedHandler;
import com.tempodb.client.Client;
import com.tempodb.client.ClientBuilder;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariable;
import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariableWithDefault;

public class StartFermonitorApp {

	static final Logger log = LoggerFactory.getLogger(StartFermonitorApp.class);
	
	public static void main(String...args) throws Exception {
		log.info("Starting FermonitorApp...");
		
		final Client tempodb = new ClientBuilder()
			.host(tempodbHost())
			.port(tempodbPort())
			.key(tempodbKey())
			.secret(tempodbSecret())
			.secure(tempodbSecure())
			.build();
		
		final IronCache ironCache = new DefaultIronCache(
			ironCacheToken(), 
			ironCacheProjectId(),
			ironCacheCacheName());
		
		final JedisPool jedisPool = new JedisPool(
			new JedisPoolConfig(), 
			jedisHost(), 
			jedisPort());
		
		final Vertx vertx = VertxFactory.newVertx();
		final HttpServer server = vertx.createHttpServer();
		final RouteMatcher router = new RouteMatcher();
		
		router
			.get("/", request -> { request.response().end("Hello, world!"); })
			.get("/brews", new ListBrews(tempodb))
			.post("/brews/:brewId", new ProtectedHandler(ironCache, new CreateBrew(tempodb)))
			.post("/brews/:brewId/temperatures", new ProtectedHandler(ironCache, new UploadTemperature(tempodb, jedisPool)));
		
		server.requestHandler(router).listen(port(), asyncResult -> {
			if (asyncResult.succeeded()) {
				log.info("HTTP server is listening.");
			} else {
				throw new RuntimeException("Failed to start HTTP server!");
			}
		});

		// block the main thread
		new CountDownLatch(1).await();
	}
	
	private static int jedisPort() throws URISyntaxException {
		URI uri = new URI(propertyOrEnvVariable("REDISCLOUD_URL"));
		return uri.getPort();
	}

	private static String jedisHost() throws URISyntaxException {
		URI uri = new URI(propertyOrEnvVariable("REDISCLOUD_URL"));
		return uri.getHost();
	}

	private static String ironCacheProjectId() {
		return propertyOrEnvVariable("IRON_CACHE_PROJECT_ID");
	}

	private static String ironCacheToken() {
		return propertyOrEnvVariable("IRON_CACHE_TOKEN");
	}
	
	private static String ironCacheCacheName() {
		return propertyOrEnvVariable("IRON_CACHE_CACHE_NAME");
	}

	private static boolean tempodbSecure() {
		return Boolean.parseBoolean(propertyOrEnvVariable("TEMPODB_API_SECURE").toLowerCase());
	}

	private static String tempodbSecret() {
		return propertyOrEnvVariable("TEMPODB_API_SECRET");
	}

	private static String tempodbKey() {
		return propertyOrEnvVariable("TEMPODB_API_KEY");
	}

	private static int tempodbPort() {
		return Integer.parseInt(propertyOrEnvVariable("TEMPODB_API_PORT"));
	}
	
	private static String tempodbHost() {
		return propertyOrEnvVariable("TEMPODB_API_HOST");
	}

	private static int port() {
		return Integer.parseInt(propertyOrEnvVariableWithDefault("PORT", "9090"));
	}
	
}
