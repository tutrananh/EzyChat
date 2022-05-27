package org.youngmonkeys.common.service;



import org.youngmonkeys.common.entity.ChatUser;

import java.util.List;
import java.util.Set;

public interface ChatUserService {

    void saveUser(ChatUser user);

    ChatUser getUser(String username);

    ChatUser createUser(String username, String password);

    List<ChatUser> getSearchUsers(Set<String> excludeUsers, String keyword);


}
