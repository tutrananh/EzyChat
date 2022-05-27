package org.youngmonkeys.common.repo;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import org.youngmonkeys.common.entity.ChatUser;


import java.util.List;
import java.util.Set;

@EzyRepository("userRepo")
public interface ChatUserRepo extends EzyMongoRepository<Long, ChatUser> {

    @EzyQuery("{$and: [{'username': {$nin: ?0}}, {'username': {$regex : ?1}}]}")
    List<ChatUser> findByUsernameRegex(Set<String> excludeUsers, String keyword);

}
