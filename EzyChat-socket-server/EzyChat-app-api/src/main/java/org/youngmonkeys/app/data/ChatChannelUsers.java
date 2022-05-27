package org.youngmonkeys.app.data;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
@AllArgsConstructor
@EzyObjectBinding(read = false)
public class ChatChannelUsers {

    protected long channelId;
    protected Set<String> users;


}
