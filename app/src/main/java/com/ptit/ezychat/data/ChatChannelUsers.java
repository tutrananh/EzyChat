package com.ptit.ezychat.data;

import java.io.Serializable;
import java.util.List;



public class ChatChannelUsers implements Serializable {

    protected long channelId;
    protected List<String> users;
    public ChatChannelUsers() {
    }

    public ChatChannelUsers(long channelId, List<String> users) {
        this.channelId = channelId;
        this.users = users;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ChatChannelUsers{" +
                "channelId=" + channelId +
                ", users=" + users +
                '}';
    }
}
