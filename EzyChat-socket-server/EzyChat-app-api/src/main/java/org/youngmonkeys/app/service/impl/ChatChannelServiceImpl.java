package org.youngmonkeys.app.service.impl;

import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import lombok.Setter;
import org.youngmonkeys.app.entity.ChatChannel;
import org.youngmonkeys.app.repo.ChatChannelRepo;
import org.youngmonkeys.app.service.ChatChannelService;


import java.util.List;

@Setter
@EzySingleton("channelService")
public class ChatChannelServiceImpl implements ChatChannelService {

    @EzyAutoBind
    protected ChatChannelRepo channelRepo;
    @EzyAutoBind
    private EzyMaxIdRepository maxIdRepository;

    @Override
    public long newChannelId() {
        long newId = maxIdRepository.incrementAndGet("chat_chanel");
        return newId;
    }

    @Override
    public void saveChannel(ChatChannel channel) {
        channelRepo.save(channel);
    }

    @Override
    public ChatChannel getChannel(long channelId) {
        return channelRepo.findByField("_id",channelId);
    }

}
