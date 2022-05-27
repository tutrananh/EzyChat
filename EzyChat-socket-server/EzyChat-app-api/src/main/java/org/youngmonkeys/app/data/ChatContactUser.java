package org.youngmonkeys.app.data;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.AllArgsConstructor;
import org.youngmonkeys.common.entity.ChatUser;

@AllArgsConstructor
@EzyObjectBinding(read = false)
public class ChatContactUser {
    private final ChatUser user;

    public String getUsername() {
        return user.getUsername();
    }

}
