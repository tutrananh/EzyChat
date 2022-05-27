package com.ptit.ezychat.socket;

import com.ptit.ezychat.constant.Commands;
import com.ptit.ezychat.model.Message;
import com.tvd12.ezyfoxserver.client.entity.EzyObject;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;

import java.util.List;

public class SocketRequests {

    private static SocketRequests INSTANCE = new SocketRequests();

    private SocketRequests(){}

    public static SocketRequests getINSTANCE() {
        return INSTANCE;
    }

    public  void sendGetContacts(){
        SocketClientProxy.getInstance().getClient().getApp().send(Commands.CHAT_GET_CONTACTS);
    }

    public  void sendGetOnlineContacts(){
        SocketClientProxy.getInstance().getClient().getApp().send(Commands.CHAT_GET_ONLINE_CONTACTS);
    }

    public  void sendSearchContacts(String keyword){
        EzyObject data = EzyEntityFactory
                .newObjectBuilder()
                .append("keyword", keyword)
                .build();
        SocketClientProxy.getInstance().getClient().getApp().send(Commands.SEARCH_NEW_CONTACTS, data);
    }

    public void sendAddContacts(List<String> usernames){
        EzyObject data = EzyEntityFactory
                .newObjectBuilder()
                .append("usernames", usernames)
                .build();
        SocketClientProxy.getInstance().getClient().getApp().send(Commands.ADD_CONTACTS, data);
    }

    public  void sendMessage(Message message){
        EzyObject data = EzyEntityFactory
                .newObjectBuilder()
                .append("message", message.getMessage())
                .append("channelId",message.getChannelId())
                .append("sender",message.getSender())
                .build();
        SocketClientProxy.getInstance().getClient().getApp().send(Commands.CHAT_USER_MESSAGE, data);
    }

    public void getMessagesOfChannel(long channelId, long maxId){
        EzyObject data = EzyEntityFactory
                .newObjectBuilder()
                .append("channelId",channelId)
                .append("maxId",maxId)
                .build();
        SocketClientProxy.getInstance().getClient().getApp().send(Commands.CHAT_CHANNEL_MESSAGE, data);
    }

    public void channelStatus(long channelId, int status){
        EzyObject data = EzyEntityFactory
                .newObjectBuilder()
                .append("channelId",channelId)
                .append("status", status)
                .build();
        SocketClientProxy.getInstance().getClient().getApp().send(Commands.CHAT_CHANNEL_STATUS, data);
    }
}
