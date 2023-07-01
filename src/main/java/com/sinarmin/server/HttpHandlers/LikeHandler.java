package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.LikeController;
import com.sinarmin.server.controllers.TweetController;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.utils.ExtractUserAuth;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class LikeHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		LikeController likeController = null;
		try {
			likeController = new LikeController();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		UserController userController = null;
		try {
			userController = new UserController();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		TweetController tweetController = null;
		try {
			tweetController = new TweetController();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath();
		String response = "This is the response likes";
		String[] splittedPath = path.split("/");
		switch (method) {
			case "GET":
				if (splittedPath.length == 2) {
					try {
						response = likeController.getAll();
					} catch (SQLException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				} else {
					if (splittedPath.length == 4 && splittedPath[2].equals("likers")) {
						try {
							response = likeController.getLikers(splittedPath[3]);
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					} else if (splittedPath.length == 4 && splittedPath[2].equals("likings")) {
						try {
							response = likeController.getLikes(splittedPath[3]);
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					} else {
						response = "wrong URL";
					}
				}
				break;
			case "POST": // POST ip:port/like/userID/tweetID
				if (splittedPath.length != 4) {
					response = "wtf";
				} else if (!userController.isUserExists(splittedPath[2])) {
					response = "user-not-found";
				} else if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
					response = "permission-denied";
				} else {
					try {
						likeController.saveLike(splittedPath[2], splittedPath[3]);
					} catch (SQLException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					response = "Done!";
				}
				break;
			case "DELETE":
				if (splittedPath.length != 4) {
					if (splittedPath.length == 2) {
						try {
							likeController.deleteAll();
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						response = "Done!";
					} else response = "wtf";
				} else {
					try {
						if (!userController.isUserExists(splittedPath[2]) || tweetController.getTweetById(splittedPath[3]) == null) {
							response = "user-not-found";
						} else if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
							response = "permission-denied";
						} else {
							try {
								likeController.deleteLike(splittedPath[2], splittedPath[3]);
							} catch (SQLException e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
							response = "Done!";
						}
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
				break;
		}
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}