package com.sinarmin.server.dataAccess;

import com.sinarmin.server.models.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageDAO {
	private final Connection connection;

	public MessageDAO() throws SQLException {
		connection = DatabaseConnectionManager.getConnection();
		createMessageTable();
	}

	public void createMessageTable() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS messages (id VARCHAR(60) PRIMARY KEY, sender VARCHAR(36), receiver VARCHAR(36), text VARCHAR(300), createdat DATE)");
		preparedStatement.executeUpdate();
	}

	public void saveMessage(Message message) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages (id, sender, receiver, text, createdat) VALUES (?, ?, ?, ?, ?)");
		preparedStatement.setString(1, message.getId());
		preparedStatement.setString(2, message.getSender());
		preparedStatement.setString(3, message.getReceiver());
		preparedStatement.setString(4, message.getText());
		preparedStatement.setDate(5, message.getCreatedAt());
		preparedStatement.executeUpdate();
	}

	public void deleteMessage(Message message) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
		preparedStatement.setString(1, message.getId());
		preparedStatement.executeUpdate();
	}//todo

	public void deleteAll() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages");
		preparedStatement.executeUpdate();
	}
}