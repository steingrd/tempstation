package com.github.steingrd.fermonitor.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.github.steingrd.fermonitor.app.JedisPoolFactory;
import com.github.steingrd.fermonitor.security.AuthorizationService;

public class CreateSecret {

	final static Logger log = LoggerFactory.getLogger(CreateSecret.class);

	public static void main(String[] args) {
		try {
			createSecret(args);
		} catch (Exception e) {
			log.error("createSecret failed!", e);
		}
	}
	
	static void createSecret(String[] args) throws Exception {
		if (args.length != 1) {
			log.info("Missing args: " + args.length);
			return;
		}
		
		final String brewId = args[0];
		
		final JedisPool jedisPool = new JedisPoolFactory().create();
		final AuthorizationService authService = new AuthorizationService(jedisPool);
		
		String secret = authService.createSecret(brewId);
		log.info("Secret for brew {} is {} ", brewId, secret);
		
	}

}