package com.sinarmin.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinarmin.server.dataAccess.MessageDAO;
import com.sinarmin.server.models.Message;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class MessageController {
	private final MessageDAO messageDAO;

	public MessageController() throws SQLException {
		messageDAO = new MessageDAO();
	}

	public void addMessage(String id, String sender, String receiver, String text) throws SQLException {
		messageDAO.saveMessage(new Message(id, sender, receiver, text));
	}

	public String getMessages(String u1, String u2) throws SQLException, JsonProcessingException {
		ArrayList<Message> messages = messageDAO.getMessages(u1, u2);
		ObjectMapper objectMapper = new ObjectMapper();
		String response = objectMapper.writeValueAsString(messages);
		return response;
	}

	public void deleteMessage(String id) throws SQLException {
		messageDAO.deleteMessage(id);
	}

	public void deleteAll() throws SQLException {
		messageDAO.deleteAll();
	}

	public String getNotify(String receiver, int cnt) throws SQLException, JsonProcessingException {
		ArrayList<Message> messages = messageDAO.getNotify(receiver);
//		Collections.sort(messages);
		cnt = Integer.min(cnt, messages.size());
		ObjectMapper objectMapper = new ObjectMapper();
		String response = objectMapper.writeValueAsString(messages.subList(messages.size() - cnt, messages.size()));
		return response;
	}
}