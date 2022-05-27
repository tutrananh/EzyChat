package com.ptit.ezychat.callback;

import com.ptit.ezychat.data.ChatChannelUsers;

import java.util.List;

public interface GetOnlineContactsCallBack {
    void onGetOnlineContactsSuccess(List<ChatChannelUsers> contacts);
}
