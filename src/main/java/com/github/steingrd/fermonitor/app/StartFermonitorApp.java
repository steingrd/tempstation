package com.github.steingrd.fermonitor.app;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.http.HttpServer;

import static com.github.steingrd.fermonitor.app.EnvironmentUtils.propertyOrEnvVariableWithDefault;

public class StartFermonitorApp {

	static final Logger log = LoggerFactory.getLogger(StartFermonitorApp.class);
	
	public static void main(String...args) throws InterruptedException {
		log.info("Starting FermonitorApp...");
		
		final Vertx vertx = VertxFactory.newVertx();
		final HttpServer server = vertx.createHttpServer();

		server.requestHandler(request -> {
			log.info("Handling request {}", request.path());
			request.response().end();
		});
		
		server.listen(port(), asyncResult -> {
			if (asyncResult.succeeded()) {
				log.info("HTTP server is listening.");
			} else {
				throw new RuntimeException("Failed to start HTTP server!");
			}
		});

		// block the main thread
		new CountDownLatch(1).await();
	}
	
	static int port() {
		return Integer.parseInt(propertyOrEnvVariableWithDefault("PORT", "9090"));
	}
	
}
