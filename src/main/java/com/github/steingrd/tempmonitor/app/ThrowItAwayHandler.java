package com.github.steingrd.tempmonitor.app;

import org.vertx.java.core.Handler;

public interface ThrowItAwayHandler<T> extends Handler<T> {

	void handleAndThrow(T event) throws Exception;
	
	default void handle(T event) {
		try {
			handleAndThrow(event);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
