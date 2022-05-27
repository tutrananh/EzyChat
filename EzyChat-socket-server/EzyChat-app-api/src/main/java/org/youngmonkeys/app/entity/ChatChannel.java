package org.youngmonkeys.app.entity;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.Data;
import org.youngmonkeys.common.entity.ChatEntity;

@Data
@EzyCollection
public class ChatChannel extends ChatEntity {
    @EzyId
    private Long id;
    private  int isPlaying;

    public ChatChannel(Long id, int isPlaying) {
        this.id = id;
        this.isPlaying = isPlaying;
    }
}
