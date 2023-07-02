package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.MessageController;
import com.sinarmin.server.utils.ExtractUserAuth;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import java.io.*;
import java.sql.SQLException;

public class MessageHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		MessageController messageController = null;
		try {
			messageController = new MessageController();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath();
		String response = "";
		String[] splittedPath = path.split("/");
		switch (method) {
			case "GET": //port:id/direct/person1/person2
				if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
					response = "permission-denied";
					break;
				}
				if (splittedPath.length == 3) { //port:id/direct/person
					try {
						response = messageController.getNotify(splittedPath[2], 20);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				}
				try {
					response = messageController.getMessages(splittedPath[2], splittedPath[3]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "POST": // ip:port/direct + body
				InputStream requestBody = exchange.getRequestBody();
				BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
				StringBuilder body = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					body.append(line);
				}
				requestBody.close();
				String newMessage = body.toString();
				JSONObject jsonObject = new JSONObject(newMessage);
				if (!jsonObject.getString("sender").equals(ExtractUserAuth.extract(exchange))) {
					response = "permission-denied";
					break;
				}
				response = "Done!";
				try {
					messageController.addMessage(jsonObject.getString("id"), jsonObject.getString("sender"), jsonObject.getString("receiver"), jsonObject.getString("text"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
		}
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}