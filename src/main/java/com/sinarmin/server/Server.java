package com.sinarmin.server;

import com.sinarmin.server.HttpHandlers.*;
import com.sinarmin.server.controllers.UserController;
import com.sinarmin.server.data_access.BioDAO;
import com.sinarmin.server.models.Bio;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        try {
            UserController userController = new UserController();

//            userController.createBio("mahdi", "welcome to my page", "adress", "www.");
//            userController.createBio("mahdi", "hmmm", "adress", "www.");
//            userController.updateBio("1", "welcome to my page", "adress", "www.");

//            userController.createUser("mahdi", "mahdi", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
//            userController.createUser("1", "ali", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
//            userController.createUser("2", "sadegh", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
//            userController.createUser("3", "javad", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
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
            throw new RuntimeException(e);
        }
    }
}

