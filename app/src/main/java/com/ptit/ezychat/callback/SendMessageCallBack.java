package com.ptit.ezychat.callback;

import com.ptit.ezychat.model.Message;

public interface SendMessageCallBack {
    public void onSendMessageSuccess(Message message);
}
