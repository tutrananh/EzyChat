package org.youngmonkeys.plugin.controller;

import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.sercurity.EzySHA256;
import com.tvd12.ezyfoxserver.constant.EzyLoginError;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import org.youngmonkeys.common.entity.ChatUser;
import org.youngmonkeys.common.service.ChatUserService;
import org.youngmonkeys.plugin.service.WelcomeService;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;

import static com.tvd12.ezyfoxserver.constant.EzyEventNames.USER_LOGIN;

@EzySingleton
@EzyEventHandler(USER_LOGIN)
public class UserLoginController extends EzyAbstractPluginEventController<EzyUserLoginEvent> {

    @EzyAutoBind
    private WelcomeService welcomeService;

    @EzyAutoBind
    private ChatUserService userService;

    @Override
    public void handle(EzyPluginContext ctx, EzyUserLoginEvent event) {
        logger.info("handle user {} login in", event.getUsername());
        EzyObject isReg = (EzyObject)event.getData();
        int isRegistry = Integer.parseInt(isReg.get("isReg").toString());
        String username = event.getUsername();
        String password = encodePassword(event.getPassword());
		ChatUser user = userService.getUser(username);
		if (user == null ) {
            if(isRegistry == 1 || username.contains("user#")){
                user = userService.createUser(username, password);
            }
            else{
                throw  new EzyLoginErrorException(EzyLoginError.INVALID_USERNAME);
            }
		}

		if (!user.getPassword().equals(password)) {
			throw new EzyLoginErrorException(EzyLoginError.INVALID_PASSWORD);
		}
		user.setOnline(true);
		userService.saveUser(user);

        logger.info("username and password match, accept user: {}", event.getUsername());
    }

    private String encodePassword(String password) {
        return EzySHA256.cryptUtfToLowercase(password);
    }
}
