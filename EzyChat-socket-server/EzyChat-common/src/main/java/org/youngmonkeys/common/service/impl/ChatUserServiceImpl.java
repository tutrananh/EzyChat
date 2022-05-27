package org.youngmonkeys.common.service.impl;

import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import org.youngmonkeys.common.entity.ChatUser;
import org.youngmonkeys.common.repo.ChatUserRepo;
import org.youngmonkeys.common.service.ChatUserService;


import java.util.List;
import java.util.Set;

@EzySingleton("userService")
public class ChatUserServiceImpl implements ChatUserService {

    @EzyAutoBind
    private ChatUserRepo userRepo;

    @EzyAutoBind
    private EzyMaxIdRepository maxIdRepo;

    @Override
    public void saveUser(ChatUser user) {
        userRepo.save(user);
    }

    @Override
    public ChatUser getUser(String username) {
        return userRepo.findByField("username", username);
    }

    @Override
    public ChatUser createUser(String username, String password) {
        ChatUser user = new ChatUser();
        user.setId(maxIdRepo.incrementAndGet("chat_user"));
        user.setUsername(username);
        user.setPassword(password);
        userRepo.save(user);
        return user;
    }

    @Override
    public List<ChatUser> getSearchUsers(Set<String> excludeUsers, String keyword) {
        String regex = ".*" + keyword + ".*";
        return userRepo.findByUsernameRegex(excludeUsers, regex);
    }


}
