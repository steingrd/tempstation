package com.github.steingrd.tempmonitor.brews;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;

import com.github.steingrd.tempmonitor.app.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

public class InfluxdbClient {

	private final String database;
	private final InfluxDB influxdb;

	public InfluxdbClient() {
		String url = Configuration.get("INFLUXDB_URL");
		String username = Configuration.get("INFLUXDB_USERNAME");
		String password = Configuration.get("INFLUXDB_PASSWORD");
		this.database = Configuration.get("INFLUXDB_DATABASE");
		this.influxdb = InfluxDBFactory.connect(url, username, password);
	}
	
	public void uploadTemperature(double temperature) {
		Serie s = new Serie.Builder("t")
			.columns("temperature")
			.values(temperature)
			.build();
		
		this.influxdb.write(database, SECONDS, s);
	}

}
