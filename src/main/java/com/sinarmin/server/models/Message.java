package com.sinarmin.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;

public class Message implements Comparable {
	@JsonProperty("id")
	private String id;

	@JsonProperty("sender")
	private String sender;

	@JsonProperty("receiver")
	private String receiver;

	@JsonProperty("text")
	private String text;

	@JsonProperty("createdAt")
	private long createdAt;

	public Message () {

	}

	public Message(String id, String sender, String receiver, String text, long createdAt) {
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
		this.createdAt = System.currentTimeMillis();
	}

	public Message(String sender, String receiver, String text) {
		this.id = sender + System.currentTimeMillis();
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.createdAt = System.currentTimeMillis();
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

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Message message = (Message) o;

		if (createdAt != message.createdAt) return false;
		if (!id.equals(message.id)) return false;
		if (!sender.equals(message.sender)) return false;
		if (!receiver.equals(message.receiver)) return false;
		return text.equals(message.text);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + sender.hashCode();
		result = 31 * result + receiver.hashCode();
		result = 31 * result + text.hashCode();
		result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
		return result;
	}

	@Override
	public int compareTo(Object o) {
		return (int)(((Message)o).createdAt - createdAt);
	}
}