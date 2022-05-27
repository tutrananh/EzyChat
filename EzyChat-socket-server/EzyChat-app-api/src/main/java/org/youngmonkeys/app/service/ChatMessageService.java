package org.youngmonkeys.app.service;


import org.youngmonkeys.app.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    long newMessageId();
    void save(ChatMessage message);
    List<ChatMessage> getMessagesOfChanel(long channelId);
    List<ChatMessage> getNewMessagesOfChanel(long channelId, long maxId);
}
