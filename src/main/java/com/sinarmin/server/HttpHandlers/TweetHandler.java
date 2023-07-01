package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.TweetController;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.utils.ExtractUserAuth;
import com.sinarmin.server.utils.JwtAuth;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import org.json.*;

import java.io.*;
import java.sql.SQLException;
import java.sql.Date;

public class TweetHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TweetController tweetController = null;
        try {
            tweetController = new TweetController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            // ip:port/tweets/
            case "POST":
                if (splittedPath.length == 3) {
                    if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
                        response = "permission-denied";
                        break;
                    }

                    InputStream requestBody = exchange.getRequestBody();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                    StringBuilder body = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        body.append(line);
                    }
                    requestBody.close();

                    // Process the user creation based on the request body
                    String newTweet = body.toString();
                    JSONObject jsonObject = new JSONObject(newTweet);
                    String tweet_id = "";
                    try {
                        tweet_id = tweetController.createTweet(jsonObject.getString("writerId"), jsonObject.getString("ownerId"), jsonObject.getString("text"), jsonObject.getString("quoteTweetId"), toStringArray(jsonObject.getJSONArray("mediaPaths")), jsonObject.getInt("likes"), jsonObject.getInt("retweets"), jsonObject.getInt("replies"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    response = tweet_id;
                }
                else
                    response = "unknown request";
                break;
            case "GET":
                if (splittedPath.length == 2) {
                    try {
                        response = tweetController.getAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else if (splittedPath.length == 3) {
                    String tweetId = splittedPath[splittedPath.length - 1];
                    try {
                        response = tweetController.getTweetById(tweetId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else
                    response = "alo!";
                break;
            case "DELETE":
                if (splittedPath.length == 2) {
                    try {
                        tweetController.deleteAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    response = "all tweets deleted";
                }
                else
                    response = "alo!";
                break;
            default:
                response = "unknown-request";
                break;
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public static String[] toStringArray(JSONArray array) {
        if(array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for(int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }
}