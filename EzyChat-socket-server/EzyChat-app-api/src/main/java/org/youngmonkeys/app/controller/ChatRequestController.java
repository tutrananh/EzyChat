package org.youngmonkeys.app.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import org.youngmonkeys.app.constant.Commands;
import org.youngmonkeys.app.constant.Errors;
import org.youngmonkeys.app.data.ChatChannelUsers;
import org.youngmonkeys.app.entity.ChatChannel;
import org.youngmonkeys.app.entity.ChatChannelUser;
import org.youngmonkeys.app.entity.ChatChannelUserId;
import org.youngmonkeys.app.entity.ChatMessage;
import org.youngmonkeys.app.request.AddContactsRequest;
import org.youngmonkeys.app.request.GetMessagesOfChannelRequest;
import org.youngmonkeys.app.request.SearchContactsRequest;
import org.youngmonkeys.app.response.ChatMessageResponse;
import org.youngmonkeys.app.service.ChatChannelService;
import org.youngmonkeys.app.service.ChatChannelUserService;
import org.youngmonkeys.app.service.ChatContactService;
import org.youngmonkeys.app.service.ChatMessageService;
import org.youngmonkeys.common.entity.ChatUser;
import org.youngmonkeys.common.service.ChatUserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EzyRequestController
public class ChatRequestController extends EzyLoggable {

    @EzyAutoBind
    private ChatChannelService chatChannelService;

    @EzyAutoBind
    private ChatChannelUserService channelUserService;

    @EzyAutoBind
    private ChatUserService chatUserService;

    @EzyAutoBind
    private ChatContactService chatContactService;

    @EzyAutoBind
    private ChatMessageService chatMessageService;

    @EzyAutoBind
    private EzyResponseFactory responseFactory;

    @EzyDoHandle(Commands.CHAT_GET_CONTACTS)
    public void getContacts(EzyUser client) {
        logger.info("Receive get contacts request");
        List<ChatChannelUsers> channels = channelUserService.getChannelsOfUser(client.getName());
        responseFactory.newArrayResponse()
                .command(Commands.CHAT_GET_CONTACTS)
                .data(channels)
                .user(client)
                .execute();
    }

    @EzyDoHandle(Commands.CHAT_GET_ONLINE_CONTACTS)
    public void getOnlineContacts(EzyUser client) {
        logger.info("Receive get online contacts request");
        List<ChatChannelUsers> channels = channelUserService.getOnlineChannelsOfUser(client.getName());
        responseFactory.newArrayResponse()
                .command(Commands.CHAT_GET_ONLINE_CONTACTS)
                .data(channels)
                .user(client)
                .execute();
    }

    @EzyDoHandle(Commands.SEARCH_NEW_CONTACTS)
    public void searchNewContacts(EzyUser client, SearchContactsRequest request) {
        logger.info("Receive search new contacts request: " + request.getKeyword());
        Set<String> contactedUsers = channelUserService.getContactedUsers(client.getName());
        List<ChatUser> chatUsers = chatUserService.getSearchUsers(contactedUsers,request.getKeyword());
        Set<String> newSearchUsers = new HashSet<>();
        for (ChatUser c:chatUsers) {
            newSearchUsers.add(c.getUsername());
        }
        System.out.println(newSearchUsers);
        responseFactory.newArrayResponse()
                .command(Commands.SEARCH_NEW_CONTACTS)
                .data(newSearchUsers)
                .user(client)
                .execute();
    }
    @EzyDoHandle(Commands.ADD_CONTACTS)
    public void addNewContacts(EzyUser client, AddContactsRequest request) {
        logger.info("Receive add new contacts request " + request.getUsernames());

        long channelId = chatChannelService.newChannelId();
        ChatChannel channel = new ChatChannel(channelId, 0);
        chatChannelService.saveChannel(channel);

        Set<String> channelUsers = new HashSet<>();
        channelUsers.addAll(request.getUsernames());
        chatContactService.addContacts(client.getName(), channelUsers);
        channelUsers.add(client.getName());

        List<ChatChannelUser> chatChannelUserList = new ArrayList<>();
        for(String s: channelUsers){
            ChatChannelUserId chatChannelUserId = new ChatChannelUserId(channelId,s);
            chatChannelUserList.add(new ChatChannelUser(chatChannelUserId));
        }
        channelUserService.saveChannelUsers(chatChannelUserList);
        ChatChannelUsers responseData = new ChatChannelUsers(channelId, request.getUsernames());
        responseFactory.newObjectResponse()
                .command(Commands.ADD_CONTACTS)
                .data(responseData)
                .user(client)
                .execute();
    }

    @EzyDoHandle(Commands.CHAT_USER_MESSAGE)
    public void sendMessage(EzyUser client, ChatMessage message) {
        logger.info("Receive new  message: " + message.toString());
        message.setId(chatMessageService.newMessageId());
        ChatChannelUsers channelUsers = channelUserService.getChannelUsers(message.getChannelId(), client.getName());
        if (channelUsers == null) {
            throw new EzyBadRequestException(Errors.CHANNEL_NOT_FOUND, "channel with id: " + message.getChannelId() + " not found");
        }
        channelUsers.getUsers().add(client.getName());
        chatMessageService.save(message);
        ChatMessageResponse messageResponse =
            new ChatMessageResponse(message.getId(),message.getMessage(),message.getCreationDate(), message.getChannelId(), message.getSender());
        System.out.println(channelUsers.getUsers());
        responseFactory.newObjectResponse()
                .command(Commands.CHAT_USER_MESSAGE)
                .data(messageResponse)
                .usernames(channelUsers.getUsers())
                .execute();
    }

    @EzyDoHandle(Commands.CHAT_CHANNEL_MESSAGE)
    public void getMessagesOfChannel(EzyUser client, GetMessagesOfChannelRequest request) {
        logger.info("Get messages of channel: " + request.getChannelId());
        List<ChatMessage> chatMessageList = chatMessageService.getNewMessagesOfChanel(request.getChannelId(), request.getMaxId());
        List<ChatMessageResponse> chatMessageResponseList = new ArrayList<>();
        for(ChatMessage message: chatMessageList){
            ChatMessageResponse messageResponse = new ChatMessageResponse(message.getId(),message.getMessage(),
                    message.getCreationDate(), message.getChannelId(), message.getSender());
            chatMessageResponseList.add(messageResponse);
        }
        responseFactory.newArrayResponse()
                .command(Commands.CHAT_CHANNEL_MESSAGE)
                .data(chatMessageResponseList)
                .user(client)
                .execute();
    }

    @EzyDoHandle(Commands.CHAT_CHANNEL_STATUS)
    public void getChannelStatus(EzyUser client, EzyObject request) {
        long channelId = Long.parseLong(request.get("channelId").toString());
        int status = Integer.parseInt(request.get("status").toString());
        System.out.println("id ne: "+channelId);
        ChatChannel channel = chatChannelService.getChannel(channelId);
        if(status == Commands.GAME_PLAYING && channel.getIsPlaying() !=1){
            channel.setIsPlaying(1);
            chatChannelService.saveChannel(channel);
        }
        if(status == Commands.GAME_ENDING && channel.getIsPlaying() !=0){
            channel.setIsPlaying(0);
            chatChannelService.saveChannel(channel);
        }

        EzyObject data = EzyEntityFactory
                .newObjectBuilder()
                .append("status",channel.getIsPlaying())
                .build();

        responseFactory.newObjectResponse()
                .command(Commands.CHAT_CHANNEL_STATUS)
                .data(data)
                .user(client)
                .execute();

    }

}
