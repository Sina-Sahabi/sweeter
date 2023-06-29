package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.FollowController;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.utils.ExtractUserAuth;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class FollowHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FollowController followController = null;
        try {
            followController = new FollowController();
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
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "This is the response follows";
        String[] splitedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splitedPath.length == 2) {
                    try {
                        response = followController.getAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else {
                    if (splitedPath.length == 4 && splitedPath[2].equals("followers")) {
                        try {
                            response = followController.getFollowers(splitedPath[3]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    } else if (splitedPath.length == 4 && splitedPath[2].equals("followings")) {
                        try {
                            response = followController.getFollows(splitedPath[3]);
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
                if (splitedPath.length != 4) {
                    response = "wtf";
                } else if (!userController.isUserExists(splitedPath[2]) || !userController.isUserExists(splitedPath[3])) {
                    response = "user-not-found";
                } else if (!splitedPath[2].equals(ExtractUserAuth.extract(exchange))) {
                    response = "permission-denied";
                } else {
                    //todo block
                    try {
                        followController.saveFollow(splitedPath[2], splitedPath[3]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    response = "Done!";
                }
                break;
            case "DELETE":
                if (splitedPath.length != 4) {
                    if (splitedPath.length == 2) {
                        try {
                            followController.deleteAll();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                        response = "Done!";
                    } else response = "wtf";
                } else if (!userController.isUserExists(splitedPath[2]) || !userController.isUserExists(splitedPath[3])) {
                    response = "user-not-found";
                } else if (!splitedPath[2].equals(ExtractUserAuth.extract(exchange))) {
                    response = "permission-denied";
                } else {
                    try {
                        followController.deleteFollow(splitedPath[2], splitedPath[3]);
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