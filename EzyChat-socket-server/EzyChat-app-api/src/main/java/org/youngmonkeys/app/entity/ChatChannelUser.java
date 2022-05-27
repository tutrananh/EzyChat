package org.youngmonkeys.app.entity;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.youngmonkeys.common.entity.ChatEntity;

@Data
@AllArgsConstructor
@EzyCollection
public class ChatChannelUser extends ChatEntity {
    @EzyId
    private ChatChannelUserId id;

}
