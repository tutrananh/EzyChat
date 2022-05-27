package org.youngmonkeys.common.entity;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.Data;

@Data
@EzyCollection
public class ChatUser extends ChatEntity {

    @EzyId
    private Long id;
    private String username;
    private String password;
    private String firstName = "";
    private String lastName = "";
    private String avatarUrl = "";
    private boolean online;

}
