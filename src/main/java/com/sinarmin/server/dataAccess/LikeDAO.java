package com.sinarmin.server.dataAccess;

import com.sinarmin.server.models.Like;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeDAO {
	private final Connection connection;
	public LikeDAO() throws SQLException {
		connection = DatabaseConnectionManager.getConnection();
		createLikeTable();
	}

	public void createLikeTable() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS likes (liker VARCHAR(36), liked VARCHAR(36), PRIMARY KEY (liker, liked))");
		preparedStatement.executeUpdate();
	}

	public void saveLike(Like like) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO likes (liker, liked) VALUES (?, ?)");
		preparedStatement.setString(1, like.getLiker());
		preparedStatement.setString(2, like.getLiked());
		preparedStatement.executeUpdate();
	}

	public void deleteLike(Like like) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM likes WHERE liker = ? AND liked = ?");
		preparedStatement.setString(1, like.getLiker());
		preparedStatement.setString(2, like.getLiked());
		preparedStatement.executeUpdate();
	}

	public void deleteAll() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM likes");
		preparedStatement.executeUpdate();
	}

	/**
	 * @param Id
	 * @return id has liked what
	 * @throws SQLException
	 */
	public List<Like> getLikes(String Id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE liker = ?");
		preparedStatement.setString(1, Id);
		ResultSet resultSet = preparedStatement.executeQuery();
		List<Like> likes = new ArrayList<>();
		while (resultSet.next()) {
			Like like = new Like();
			like.setLiker(resultSet.getString("liker"));
			like.setLiked(resultSet.getString("liked"));
			likes.add(like);
		}
		return likes;
	}

	/**
	 * @param Id
	 * @return what has liked id
	 * @throws SQLException
	 */
	public List<Like> getLikers(String Id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE liked = ?");
		preparedStatement.setString(1, Id);
		ResultSet resultSet = preparedStatement.executeQuery();
		List<Like> likes = new ArrayList<>();
		while (resultSet.next()) {
			Like like = new Like();
			like.setLiker(resultSet.getString("liker"));
			like.setLiked(resultSet.getString("liked"));
			likes.add(like);
		}
		return likes;
	}

	public List<Like> getAll() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes");
		ResultSet resultSet = preparedStatement.executeQuery();
		List<Like> likes = new ArrayList<>();
		while (resultSet.next()) {
			Like like = new Like();
			like.setLiker(resultSet.getString("liker"));
			like.setLiked(resultSet.getString("liked"));
			likes.add(like);
		}
		return likes;
	}

	public boolean isLiking(String likerId, String likedId) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE liker = ? AND liked = ?");
		preparedStatement.setString(1, likerId);
		preparedStatement.setString(2, likedId);
		ResultSet resultSet = preparedStatement.executeQuery();
		boolean isLiking = resultSet.next();
		return isLiking;
	}
}