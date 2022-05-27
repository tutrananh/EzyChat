package org.youngmonkeys.app.service;



import org.youngmonkeys.app.entity.ChatChannel;

import java.util.List;

public interface ChatChannelService {

    long newChannelId();

    void saveChannel(ChatChannel channel);

    ChatChannel getChannel(long channelId);

}
