package com.sinarmin.Server;

import com.sun.net.httpserver.HttpServer;
import com.sinarmin.Server.HttpHandlers.FollowHandler;
import com.sinarmin.Server.HttpHandlers.TweetHandler;
import com.sinarmin.Server.HttpHandlers.UserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
	public static void main(String[] args) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			server.createContext("/users", new UserHandler());
			server.createContext("/tweets", new TweetHandler());
			server.createContext("/follows", new FollowHandler());
			server.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

