package org.youngmonkeys.app.repo;


import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import org.youngmonkeys.app.entity.ChatMessage;

import java.util.List;


@EzyAutoImpl("messageRepo")
public interface ChatMessageRepo extends EzyMongoRepository<Long, ChatMessage> {
    @EzyQuery("{$and: [{'channelId': ?0}, {'_id': {$gt: ?1}}]}")
    List<ChatMessage> findNewMessagesByChannelId(long channelId, long maxId);


}
