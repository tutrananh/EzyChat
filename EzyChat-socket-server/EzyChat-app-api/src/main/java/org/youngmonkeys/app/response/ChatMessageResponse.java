package org.youngmonkeys.app.response;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@EzyObjectBinding
public class ChatMessageResponse {
    private long id;
    private String message;
    private String time;
    private long channelId;
    private String sender;

    public ChatMessageResponse(long id, String message, Date time, long channelId, String sender) {

        this.id = id;

        this.message = message;

        String messageTime = new SimpleDateFormat("HH:mm").format(time);
        this.time = messageTime;

        this.channelId = channelId;

        this.sender = sender;
    }
}

