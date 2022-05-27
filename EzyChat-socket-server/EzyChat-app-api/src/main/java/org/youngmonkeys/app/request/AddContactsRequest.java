package org.youngmonkeys.app.request;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Data;

import java.util.Set;

@Data
@EzyObjectBinding
public class AddContactsRequest {
    Set<String> usernames;
}
