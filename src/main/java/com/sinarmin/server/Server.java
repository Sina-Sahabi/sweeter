package com.sinarmin.server;

import com.sinarmin.server.HttpHandlers.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    public static void main(String[] args) {
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
            server.createContext("/block", new BlockHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}