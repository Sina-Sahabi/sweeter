package com.sinarmin.server.HttpHandlers;

import com.sinarmin.server.controllers.HashtagController;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.models.Hashtag;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Handler;

public class HashtagHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashtagController hashtagController = null;
        try {
            hashtagController = new HashtagController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");

        switch (method) {
            case "PUT": {
                String hashtagid = splittedPath[splittedPath.length - 2];
                String tweetId = splittedPath[splittedPath.length - 1];
                Hashtag hashtag = null;
                try {
                    hashtag = hashtagController.getHashtag(hashtagid);
                    if (hashtag == null) {
                        hashtagController.createHashtag(hashtagid);
                        hashtag = hashtagController.getHashtag(hashtagid);
                    }
                    hashtag.setHashtagTweetsId(tweetId);
                    hashtagController.updateHashtag(hashtag);
                    response = "hashtag saved!";
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    response = "database error";
                }
                break;
            }
            case "get": {
                String hashtagid = splittedPath[splittedPath.length - 1];
                try {
                    response = hashtagController.JsonGetHashtag(hashtagid);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
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
            arr[i]=array.optString(i);
        return arr;
    }
}
