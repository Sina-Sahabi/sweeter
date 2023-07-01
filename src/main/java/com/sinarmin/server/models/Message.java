package com.sinarmin.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;

public class Message {
	@JsonProperty("id")
	private String id;

	@JsonProperty("sender")
	private String sender;

	@JsonProperty("receiver")
	private String receiver;

	@JsonProperty("text")
	private String text;

	@JsonProperty("createdAt")
	private Date createdAt;

	public Message () {

	}

	public Message(String id, String sender, String receiver, String text, Date createdAt) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.createdAt = createdAt;
	}

	public Message(String id, String sender, String receiver, String text) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.createdAt = new Date(System.currentTimeMillis());
	}

	public Message(String sender, String receiver, String text) {
		this.id = sender + System.currentTimeMillis();
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.createdAt = new Date(System.currentTimeMillis());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id='" + id + '\'' +
				", sender='" + sender + '\'' +
				", receiver='" + receiver + '\'' +
				", text='" + text + '\'' +
				", createdAt=" + createdAt +
				'}';
	}
}