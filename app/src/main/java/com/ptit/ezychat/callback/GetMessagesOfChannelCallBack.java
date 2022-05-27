package com.ptit.ezychat.callback;

import com.ptit.ezychat.model.Message;

import java.util.List;

public interface GetMessagesOfChannelCallBack {
    public void onGetMessagesOfChannelSuccess(List<Message> messageList);
    public void onGetChannelStatus(int status);

}
