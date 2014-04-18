package com.github.steingrd.tempmonitor.app;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;

import redis.clients.jedis.JedisPool;

import com.github.steingrd.tempmonitor.brews.ListBrews;
import com.github.steingrd.tempmonitor.brews.Temperatures;
import com.github.steingrd.tempmonitor.brews.UploadTemperature;
import com.github.steingrd.tempmonitor.security.ProtectedHandler;
import com.tempodb.client.Client;

import static com.github.steingrd.tempmonitor.app.Configuration.get;

public class StartApp {

	static final Logger log = LoggerFactory.getLogger(StartFermonitorApp.class);
	
	public static void main(String...args) throws Exception {
		log.info("Starting...");
		
		final Client tempodb = new TempoDbFactory().create();
		final JedisPool jedisPool = new JedisPoolFactory().create();
		
		final Vertx vertx = VertxFactory.newVertx();
		final HttpServer server = vertx.createHttpServer();
		final RouteMatcher router = new RouteMatcher();
		final FeatureToggle featureToggle = new FeatureToggle();
		
		router
			.get("/", request -> {
					String base = staticResources(featureToggle);
					request.response().sendFile(base + "/index.html"); 
				})
			.get ("/brews", new ListBrews(jedisPool))
			.get ("/brews/:brewId/temperatures", new Temperatures(tempodb, jedisPool))
			.post("/brews/:brewId/temperatures", 
					new ProtectedHandler(jedisPool, new UploadTemperature(vertx, tempodb, jedisPool)))
			.noMatch(request -> {
					String base = staticResources(featureToggle);
					request.response().sendFile(base + request.path(), base + "/404.html");
				});
		
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

	private static String staticResources(final FeatureToggle featureToggle) {
		String base = "src/main/resources/webapp";
		if (featureToggle.useClasspathForStaticResources()) {
			base = "target/classes/webapp";
		}
		return base;
	}

	private static int port() {
		return Integer.parseInt(get("PORT", "9090"));
	}
	
}
