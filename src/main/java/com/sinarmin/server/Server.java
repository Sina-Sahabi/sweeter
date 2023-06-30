package com.sinarmin.server;

import com.sinarmin.server.HttpHandlers.*;
import com.sinarmin.server.controllers.TweetController;
import com.sinarmin.server.dataAccess.TweetDAO;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
//        try {
////            TweetDAO t = new TweetDAO();
////            t.deletDB();
//            TweetController tweetCon = new TweetController();
//            tweetCon.createTweet("mahdi", "mahdi", "zendegi zibas", null, new String[]{"xd", "yx"}, 0, 0, 0);
//            tweetCon.createTweet("mahdi", "mahdi", "zendegi zibas1", null, null, 0, 0, 0);
//            tweetCon.createTweet("mahdi", "mahdi", "zendegi zibas2", null, null, 0, 0, 0);
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        System.exit(0);
        try {
            Files.createDirectories(Paths.get("src/main/java/com/sinarmin/server/assets"));
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/bios", new BioHandler());
            server.createContext("/users", new UserHandler());
            server.createContext("/tweets", new TweetHandler());
            server.createContext("/follows", new FollowHandler());
            server.createContext("/sessions", new SessionHandler());
            server.createContext("/media", new MediaHandler());
            server.createContext("/hashtag", new HashtagHandler());
            server.createContext("/like", new LikeHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}