package org.youngmonkeys.app.entity;


import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.youngmonkeys.common.entity.ChatEntity;

@Data
@EzyCollection
@AllArgsConstructor
@NoArgsConstructor
@EzyObjectBinding(subTypeClasses = {ChatEntity.class})
public class ChatMessage extends ChatEntity {
    @EzyId
    private Long id;
    private boolean read;
    private String message;
    private long channelId;
    private String sender;

}
