package org.youngmonkeys.app.entity;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@EzyId
@NoArgsConstructor
@AllArgsConstructor
public class ChatChannelUserId {

    private Long channelId;
    private String user;

    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<ChatChannelUserId>()
            .function(t -> t.channelId)
            .function(t -> t.user)
            .isEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return new EzyHashCodes()
            .append(channelId)
            .append(user)
            .toHashCode();
    }

}
