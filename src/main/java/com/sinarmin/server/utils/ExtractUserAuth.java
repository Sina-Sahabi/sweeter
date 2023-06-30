package com.sinarmin.server.utils;

import com.sun.net.httpserver.HttpExchange;
import java.util.List;

public class ExtractUserAuth {
	public static String extract(HttpExchange exchange) {
		List<String> list = exchange.getRequestHeaders().get("JWT");
		System.out.println(list);
		if (list == null || list.size() != 2 || !JwtAuth.jws(list.get(0)).equals(list.get(1))) return null;
		return list.get(0);
	}
}
