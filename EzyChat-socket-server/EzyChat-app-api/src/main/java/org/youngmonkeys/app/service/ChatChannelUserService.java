package org.youngmonkeys.app.service;



import org.youngmonkeys.app.data.ChatChannelUsers;
import org.youngmonkeys.app.entity.ChatChannelUser;

import java.util.List;
import java.util.Set;

public interface ChatChannelUserService {

    void saveChannelUsers(List<ChatChannelUser> channelUsers);

    ChatChannelUser getChannelUser(long channelId, String user);

    ChatChannelUsers getChannelUsers(long channelId, String user);

    List<Long> getChannelsIdOfUser(String user);

    List<ChatChannelUsers> getChannelsOfUser(String user);

    List<ChatChannelUsers> getChannelsOfUser(List<Long> channelIds, String user);

    List<ChatChannelUsers> getOnlineChannelsOfUser(String user);

    List<ChatChannelUsers> getOnlineChannelsOfUser(List<Long> channelIds, String user);

    Set<String> getContactedUsers(String user);

}
