package org.youngmonkeys.app.request;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Data;

@Data
@EzyObjectBinding
public class GetMessagesOfChannelRequest {
    private long channelId;
    private long maxId;
}

