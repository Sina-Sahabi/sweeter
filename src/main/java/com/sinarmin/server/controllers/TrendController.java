package com.sinarmin.server.controllers;

import com.sinarmin.server.dataAccess.HashtagDAO;
import com.sinarmin.server.dataAccess.TweetDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrendController {
	private final HashtagDAO hashtagDAO;
	private final TweetDAO tweetDAO;

	public TrendController() throws SQLException {
		hashtagDAO = new HashtagDAO();
		tweetDAO = new TweetDAO();
	}

	public ArrayList<String> getTweetsByDate(Date date) throws SQLException {
		return tweetDAO.getTweetsByDate(date);
	}

	public ArrayList<String> getTags(String id) throws SQLException {
		return hashtagDAO.getTags(id);
	}
}
