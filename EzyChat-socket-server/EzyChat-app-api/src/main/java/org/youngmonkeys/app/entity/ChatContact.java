package org.youngmonkeys.app.entity;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.Data;
import org.youngmonkeys.common.entity.ChatEntity;

@Data
@EzyCollection
public class ChatContact extends ChatEntity {
    @EzyId
    private ChatContactId id;
}
