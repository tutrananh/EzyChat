package com.ptit.ezychat;

import android.app.Application;

import com.tvd12.ezyfoxserver.client.logger.EzyLogger;
import com.tvd12.ezyfoxserver.client.socket.EzyMainEventsLoop;

public class ChatApplication extends Application {
    EzyMainEventsLoop mainEventsLoop;
    public  ChatApplication(){
        System.out.println("day");
        EzyLogger.setLevel(EzyLogger.LEVEL_DEBUG);
        this.mainEventsLoop = new  EzyMainEventsLoop();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("ne");
        this.mainEventsLoop.start();
    }
}
