package com.ptit.ezychat.model;

import java.io.Serializable;

public class Message implements Serializable {
    private long id;
    private String message;
    private String time;
    private long channelId;
    private String sender;

    public Message() {}

    public Message(long id, String message, String time, long channelId, String sender) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.channelId = channelId;
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", channelId=" + channelId +
                ", sender='" + sender + '\'' +
                '}';
    }
}
