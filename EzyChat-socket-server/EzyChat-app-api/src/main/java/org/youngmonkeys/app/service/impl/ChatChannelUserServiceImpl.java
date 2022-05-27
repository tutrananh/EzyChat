package org.youngmonkeys.app.service.impl;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.io.EzyLists;
import com.tvd12.ezyfox.util.EzyHashMapSet;
import com.tvd12.ezyfox.util.EzyMapSet;

import lombok.Setter;
import org.youngmonkeys.app.data.ChatChannelUsers;
import org.youngmonkeys.app.entity.ChatChannelUser;
import org.youngmonkeys.app.entity.ChatChannelUserId;
import org.youngmonkeys.app.repo.ChatChannelUserRepo;
import org.youngmonkeys.app.service.ChatChannelUserService;
import org.youngmonkeys.common.entity.ChatUser;
import org.youngmonkeys.common.service.ChatUserService;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@EzySingleton("channelUserService")
public class ChatChannelUserServiceImpl implements ChatChannelUserService {

    @EzyAutoBind
    protected ChatChannelUserRepo channelUserRepo;

    @EzyAutoBind
    protected ChatUserService chatUserService;
    @Override
    public void saveChannelUsers(List<ChatChannelUser> channelUsers) {
        channelUserRepo.save(channelUsers);
    }

    @Override
    public ChatChannelUser getChannelUser(long channelId, String user) {
        return channelUserRepo.findById(new ChatChannelUserId(channelId, user));
    }

    @Override
    public ChatChannelUsers getChannelUsers(long channelId, String user) {
        List<ChatChannelUser> list = channelUserRepo.findByChannelId(channelId, user);
        Map<Long, Set<String>> map = mapChannelUsers(list);
        return new ChatChannelUsers(channelId, map.get(channelId));
    }

    @Override
    public List<Long> getChannelsIdOfUser(String user) {
        List<ChatChannelUser> list = channelUserRepo.findByUser(user);
        List<Long> answer = EzyLists.newArrayList(list, i -> i.getId().getChannelId());
        return answer;
    }

    @Override
    public List<ChatChannelUsers> getChannelsOfUser(String user) {
        List<Long> channelIds = getChannelsIdOfUser(user);
        List<ChatChannelUsers> answer = getChannelsOfUser(channelIds, user);
        return answer;
    }

    @Override
    public List<ChatChannelUsers> getChannelsOfUser(List<Long> channelIds, String user) {
        List<ChatChannelUser> list = channelUserRepo.findByChannelIds(channelIds, user);
        Map<Long, Set<String>> map = mapChannelUsers(list);
        List<ChatChannelUsers> answer = new ArrayList<>();
        for (Long channelId : map.keySet()) {
            answer.add(new ChatChannelUsers(channelId, map.get(channelId)));
        }
        return answer;
    }

    @Override
    public List<ChatChannelUsers> getOnlineChannelsOfUser(String user) {
        List<Long> channelIds = getChannelsIdOfUser(user);
        List<ChatChannelUsers> answer = getOnlineChannelsOfUser(channelIds, user);
        return answer;
    }

    @Override
    public List<ChatChannelUsers> getOnlineChannelsOfUser(List<Long> channelIds, String user) {
        List<ChatChannelUser> list = channelUserRepo.findByChannelIds(channelIds, user);
        List<ChatChannelUser> resultList = new ArrayList<>();
        for(ChatChannelUser c: list){
            ChatUser chatUser = chatUserService.getUser(c.getId().getUser());
            if(chatUser.isOnline()){
                resultList.add(c);
            }
        }
        Map<Long, Set<String>> map = mapChannelUsers(resultList);
        List<ChatChannelUsers> answer = new ArrayList<>();
        for (Long channelId : map.keySet()) {
            answer.add(new ChatChannelUsers(channelId, map.get(channelId)));
        }
        return answer;
    }

    @Override
    public Set<String> getContactedUsers(String user) {
        List<ChatChannelUsers> channels = getChannelsOfUser(user);
        Set<String> contactedUsers = new HashSet<>();
        contactedUsers.add(user);
        contactedUsers.addAll(
            channels.stream()
                .map(it -> it.getUsers())
                .flatMap(Set::stream)
                .collect(Collectors.toSet())
        );
        return contactedUsers;
    }

    protected Map<Long, Set<String>> mapChannelUsers(List<ChatChannelUser> list) {
        EzyMapSet<Long, String> map = new EzyHashMapSet<>();
        for (ChatChannelUser item : list) {
            map.addItem(item.getId().getChannelId(), item.getId().getUser());
        }
        return map;
    }

}
