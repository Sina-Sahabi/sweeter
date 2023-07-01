package com.sinarmin.server.controllers;

import com.sinarmin.server.dataAccess.HashtagDAO;
import java.sql.SQLException;
import java.util.ArrayList;

public class HashtagController {
    private final HashtagDAO hashtagDAO;

    public HashtagController() throws SQLException {
        hashtagDAO = new HashtagDAO();
    }

    public void addHashtag(String id, String tweet) throws SQLException {
        hashtagDAO.saveHashtag(id, tweet);
    }

    public void deleteAll() throws SQLException {
        hashtagDAO.deleteAll();
    }

    public String GetHashtag(String id) throws SQLException {
        ArrayList<String> tweets = hashtagDAO.getHashtag(id);
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < tweets.size(); i++) {
            if (i > 0) response.append(',');
            response.append(tweets.get(i));
        }
        return response.toString();
    }
}