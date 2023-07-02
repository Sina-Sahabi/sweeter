package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.TrendController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TrendHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		TrendController trendController = null;
		try {
			trendController = new TrendController();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath();
		String response = "";
		String[] splittedPath = path.split("/"); // ip:port/trend/.../.../...

		if (!method.equals("GET") || splittedPath.length > 5) {
			exchange.sendResponseHeaders(404, response.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}

		HashMap<String, HashMap<Integer, Double>> mp = new HashMap<>();
		long thisMonth = System.currentTimeMillis() / (1000L * 3600L * 24L * 30L);
		for (int i = 2; i < splittedPath.length; i++) {
			mp.put(splittedPath[i], new HashMap<>());
			for (int j = 0; j < 30; j++) {
				mp.get(splittedPath[i]).put(j, 0d);
			}
		}
		for (int j = 0; j < 30; j++) {
			ArrayList<String> tweets;
			try {
				tweets = trendController.getTweetsByDate(new Date(thisMonth + 1000L * 3600L * 24L * j));
				for (String str : tweets) {
					ArrayList<String> tags = trendController.getTags(str);
					for (int i = 2; i < splittedPath.length; i++) {
						if (tags.contains(splittedPath[i])) {
							mp.get(splittedPath[i]).put(29 - j, mp.get(splittedPath[i]).get(29 - j) + 1);
						}
					}
				}
				for (int i = 2; i < splittedPath.length; i++) {
					mp.get(splittedPath[i]).put(29 - j, mp.get(splittedPath[i]).get(29 - j) / (double)tweets.size());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/*
		// Create raw data.
        Map<Integer, String> data = new HashMap<Integer, String>();
        data.put(1, "hello");
        data.put(2, "world");
        System.out.println(data.toString());

        // Convert Map to byte array
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(data);

        // Parse byte array to Map
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        Map<Integer, String> data2 = (Map<Integer, String>) in.readObject();
        System.out.println(data2.toString());
		*/

		exchange.sendResponseHeaders(200, mp.toString().getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(mp.toString().getBytes());
		os.close();
	}
}