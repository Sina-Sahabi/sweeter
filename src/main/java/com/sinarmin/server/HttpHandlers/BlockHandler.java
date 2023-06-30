package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.BlockController;
import com.sinarmin.server.controllers.FollowController;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.utils.ExtractUserAuth;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class BlockHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		BlockController blockController = null;
		try {
			blockController = new BlockController();
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
		FollowController followController = null;
		try {
			followController = new FollowController();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath();
		String response = "This is the response blocks";
		String[] splittedPath = path.split("/");
		switch (method) {
			case "GET":
				if (splittedPath.length == 2) {
					try {
						response = blockController.getAll();
					} catch (SQLException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				} else {
					if (splittedPath.length == 4 && splittedPath[2].equals("blockers")) {
						try {
							response = blockController.getBlockers(splittedPath[3]);
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					} else if (splittedPath.length == 4 && splittedPath[2].equals("blockings")) {
						try {
							response = blockController.getBlocks(splittedPath[3]);
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					} else {
						response = "wrong URL";
					}
				}
				break;
			case "POST":
				if (splittedPath.length != 4) {
					response = "wtf";
				} else if (!userController.isUserExists(splittedPath[2]) || !userController.isUserExists(splittedPath[3])) {
					response = "user-not-found";
				} else if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
					response = "permission-denied";
				} else {
					try {
						blockController.saveBlock(splittedPath[2], splittedPath[3]);
						followController.deleteFollow(splittedPath[2], splittedPath[3]);
						followController.deleteFollow(splittedPath[3], splittedPath[2]);
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
							blockController.deleteAll();
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						response = "Done!";
					} else response = "wtf";
				} else if (!userController.isUserExists(splittedPath[2]) || !userController.isUserExists(splittedPath[3])) {
					response = "user-not-found";
				} else if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
					response = "permission-denied";
				} else {
					try {
						blockController.deleteBlock(splittedPath[2], splittedPath[3]);
					} catch (SQLException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					response = "Done!";
				}
				break;
		}
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}