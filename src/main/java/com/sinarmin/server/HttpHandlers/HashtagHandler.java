package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.HashtagController;
import com.sinarmin.server.controllers.TweetController;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.utils.ExtractUserAuth;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class HashtagHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashtagController hashtagController = null;
        try {
            hashtagController = new HashtagController();
        } catch (SQLException e) {
            e.printStackTrace();
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
        String response = "";
        String[] splittedPath = path.split("/");

        switch (method) {
            case "GET": // ip:port/hashtag/tagName
                if (splittedPath.length != 3) {
                    response = "wtf";
                    break;
                }
                try {
                    response = hashtagController.GetHashtag(splittedPath[2]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "POST": // ip:port/hashtag/tagName/tweetId
                if (splittedPath.length != 4) {
                    response = "wtf";
                    break;
                }
                try {
                    if (tweetController.getTweetById(splittedPath[3]) == null) {
                        response = "tweet-not-found";
                    } else if (!userController.isUserExists(ExtractUserAuth.extract(exchange))) {
                        response = "permission-denied";
                    } else {
                        try {
                            hashtagController.addHashtag(splittedPath[2], splittedPath[3]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                        response = "Done!";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "DELETE":
                try {
                    hashtagController.deleteAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                response = "Done!";
                break;
            default:
                break;
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
