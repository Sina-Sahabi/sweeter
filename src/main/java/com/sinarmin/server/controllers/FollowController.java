package com.sinarmin.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinarmin.server.dataAccess.FollowDAO;
import com.sinarmin.server.models.Follow;
import java.sql.SQLException;
import java.util.List;

public class FollowController {
    private final FollowDAO followDAO;
    public FollowController() throws SQLException {
        followDAO = new FollowDAO();
    }

    public void createFollowTable() throws SQLException {
        followDAO.createFollowTable();
    }

    public void saveFollow(String follower, String followed) throws SQLException {
        Follow follow = new Follow(follower, followed);
        followDAO.saveFollow(follow);
    }

    public void deleteFollow(String follower, String followed) throws SQLException {
        Follow follow = new Follow(follower, followed);
        followDAO.deleteFollow(follow);
    }

    public void deleteAll() throws SQLException {
        followDAO.deleteAll();
    }

    public String getFollows(String userId) throws SQLException, JsonProcessingException {
        List<Follow> follows = followDAO.getFollows(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getFollowers(String userId) throws SQLException, JsonProcessingException {
        List<Follow> follows = followDAO.getFollowers(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getAll() throws SQLException, JsonProcessingException {
        List<Follow> follows = followDAO.getAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }
}