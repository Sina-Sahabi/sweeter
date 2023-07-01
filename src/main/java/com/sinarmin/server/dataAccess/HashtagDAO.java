package com.sinarmin.server.dataAccess;

import java.sql.*;
import java.util.ArrayList;

public class HashtagDAO {
    private final Connection connection;
    public HashtagDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createHashtagTable();
    }
    public void createHashtagTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS tags (id VARCHAR(280) PRIMARY KEY, tweet VARCHAR(36), PRIMARY KEY (id, tweet))");
        statement.executeUpdate();
    }

    public void saveHashtag(String id, String tweet) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (id, tweet) VALUES (?, ?)");
        statement.setString(1, id);
        statement.setString(2, tweet);
        statement.executeUpdate();
    }

    public void deleteAll() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM tags");
        statement.executeUpdate();
    }

    public ArrayList<String> getHashtag(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> tweets = new ArrayList<>();
        while (resultSet.next()) {
            tweets.add(resultSet.getString("tweet"));
        }
        return tweets;
    }
}