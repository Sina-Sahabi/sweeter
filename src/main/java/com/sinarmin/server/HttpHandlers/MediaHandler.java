package com.sinarmin.server.HttpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.utils.ExtractUserAuth;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Collections;

public class MediaHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		UserController userController = null;
		try {
			userController = new UserController();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String response = "";
		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath();
		String[] splittedPath = path.split("/");
		// ip:port/media/userID/mediaName/mediaType
		if (splittedPath.length != 5) {
			response = "unknown-request";
		} else switch (method) {
			case "GET":
				File file;
				file = new File("src/main/java/com/sinarmin/server/assets/" + splittedPath[2] + "/" + splittedPath[3] + "." + splittedPath[4]);
				if (!file.exists()) {
					response = "no file";
					break;
				}
				exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(splittedPath[4]));
				exchange.sendResponseHeaders(200, file.length());
				OutputStream outputStream = exchange.getResponseBody();
				Files.copy(file.toPath(), outputStream);
				outputStream.close();
				return;
			case "POST":
				if (!userController.isUserExists(splittedPath[2])) {
					response = "user-not-found";
					break;
				}
				if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
					response = "permission-denied";
					break;
				}
				Files.copy(exchange.getRequestBody(), Path.of("src/main/java/com/sinarmin/server/assets/" , splittedPath[2], splittedPath[3] + "." + splittedPath[4]), StandardCopyOption.REPLACE_EXISTING);
				exchange.getRequestBody().close();
				response = "done";
				break;
			default:
				response = "unknown-request2";
				break;
		}

		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}