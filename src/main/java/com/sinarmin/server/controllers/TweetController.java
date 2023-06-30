package com.sinarmin.server.controllers;

import java.sql.Date;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinarmin.server.dataAccess.TweetDAO;
import com.sinarmin.server.models.Follow;
import com.sinarmin.server.models.Tweet;

import java.time.LocalDate;
import java.util.List;

public class TweetController {
    private final TweetDAO tweetDAO;

    public TweetController() throws SQLException {
        tweetDAO = new TweetDAO();
    }

    public String createTweet(String writerId, String ownerId, String text, String quoteTweetId, String[] mediaPaths, int likes, int retweets, int replies) throws SQLException {
        Tweet tweet = new Tweet();

        tweet.setId(String.format("%d", tweetDAO.NumberOfTweets()));
        Tweet.IncTotalNumOfTweets();
        tweet.setWriterId(writerId);
        tweet.setOwnerId(ownerId);
        tweet.setText(text);
        tweet.setQuoteTweetId(quoteTweetId);
        tweet.setMediaPaths(mediaPaths);
        tweet.setLikes(likes);
        tweet.setRetweets(retweets);
        tweet.setReplies(replies);
        tweet.setCreatedAt(Date.valueOf(LocalDate.now()));
        tweetDAO.saveTweet(tweet);
        return tweet.getId();
    }

    public void updateTweet(String writerId, String ownerId, String text, String quoteTweetId, String[] mediaPaths, int likes, int retweets, int replies) throws SQLException {
        Tweet tweet = new Tweet();

        tweet.setWriterId(writerId);
        tweet.setOwnerId(ownerId);
        tweet.setText(text);
        tweet.setQuoteTweetId(quoteTweetId);
        tweet.setMediaPaths(mediaPaths);
        tweet.setLikes(likes);
        tweet.setRetweets(retweets);
        tweet.setReplies(replies);

        tweetDAO.updateTweet(tweet);
    }

    public void deleteTweet(String id) throws SQLException {
        tweetDAO.deleteTweet(id);
    }

    public void getTweetByWriterId(String writerId) throws SQLException {
        tweetDAO.getTweetByWriterId(writerId);
    }

    public void getTweetByOwnerId(String ownerId) throws SQLException {
        tweetDAO.getTweetByOwnerId(ownerId);
    }

    public Tweet getTweet(String id) throws SQLException {
        return tweetDAO.getTweet(id);
    }
    public String getTweetById(String id) throws SQLException, JsonProcessingException {
        Tweet tweet = getTweet(id);
        if (tweet == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(tweet);
        return response;
    }

    public String getAll() throws SQLException, JsonProcessingException {
        List<Tweet> tweets = tweetDAO.getAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(tweets);
    }
    public void deleteAll() throws SQLException {
        tweetDAO.deleteAll();
    }
}