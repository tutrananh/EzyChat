package org.youngmonkeys.app.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;
import org.youngmonkeys.common.entity.ChatUser;
import org.youngmonkeys.common.service.ChatUserService;

import static com.tvd12.ezyfoxserver.constant.EzyEventNames.USER_REMOVED;

@EzySingleton
@EzyEventHandler(USER_REMOVED) // refer EzyEventType
public class UserRemovedController extends EzyAbstractAppEventController<EzyUserRemovedEvent> {
    @EzyAutoBind
    private ChatUserService chatUserService;

    @Override
    public void handle(EzyAppContext ezyAppContext, EzyUserRemovedEvent ezyUserRemovedEvent) {
        ChatUser user = chatUserService.getUser(ezyUserRemovedEvent.getUser().getName());
        user.setOnline(false);
        chatUserService.saveUser(user);
    }
}
