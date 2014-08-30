package com.github.steingrd.tempmonitor.brews;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.github.steingrd.tempmonitor.app.Configuration;

public class GraphiteClient {

	public void uploadTemperature(double temperature) throws IOException {
		String apikey = Configuration.get("HOSTEDGRAPHITE_APIKEY");
		Socket conn  = new Socket("carbon.hostedgraphite.com", 2003);
		try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
			dos.writeBytes(apikey + ".temperature " + Double.toString(temperature) + "\n");
		} finally {
			conn.close();
		}
		
	}

}
