package org.youngmonkeys.app.service.impl;

import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import lombok.Setter;
import org.youngmonkeys.app.entity.ChatMessage;
import org.youngmonkeys.app.repo.ChatMessageRepo;
import org.youngmonkeys.app.service.ChatMessageService;

import java.util.List;

@Setter
@EzySingleton("chatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService {
    @EzyAutoBind
    protected ChatMessageRepo chatMessageRepo;

    @EzyAutoBind
    private EzyMaxIdRepository maxIdRepository;

    @Override
    public long newMessageId() {
        long newId = maxIdRepository.incrementAndGet("chat_message");
        return newId;
    }

    @Override
    public void save(ChatMessage message) {
        chatMessageRepo.save(message);
    }

    @Override
    public List<ChatMessage> getMessagesOfChanel(long channelId) {
        return chatMessageRepo.findListByField("channelId", channelId);
    }

    @Override
    public List<ChatMessage> getNewMessagesOfChanel(long channelId, long maxId) {
        return chatMessageRepo.findNewMessagesByChannelId(channelId, maxId);
    }
}
