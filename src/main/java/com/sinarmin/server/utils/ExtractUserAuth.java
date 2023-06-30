package com.sinarmin.server.utils;

import com.sun.net.httpserver.HttpExchange;
import java.util.List;

public class ExtractUserAuth {
	public static String extract(HttpExchange exchange) {
		List<String> list = exchange.getRequestHeaders().get("JWT");
		System.out.println(list);
		if (list == null || list.size() != 1) return null;
		String[] split = list.get(0).split(",");
		if (split.length != 2)
				return null;
		if (!JwtAuth.jws(split[0]).equals(split[1]))
			return null;
		return split[0];
	}
}
