package com.sinarmin.server;

import com.sinarmin.server.HttpHandlers.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) {
//        try {
//            UserController userController = new UserController();
//            userController.createUser("mahdi", "mahdi", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
//            userController.createUser("1", "ali", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
//            userController.createUser("2", "sadegh", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
//            userController.createUser("3", "javad", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
//        } catch (SQLException e) {
//            e.printStackTrace(); e.printStackTrace();
//        }
//        System.exit(0);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/bios", new BioHandler());
            server.createContext("/users", new UserHandler());
            server.createContext("/tweets", new TweetHandler());
            server.createContext("/follows", new FollowHandler());
            server.createContext("/sessions", new SessionHandler());
            server.createContext("/media", new MediaHandler());
            server.createContext("/hashtag", new HashtagHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace(); e.printStackTrace();
        }
    }
}

