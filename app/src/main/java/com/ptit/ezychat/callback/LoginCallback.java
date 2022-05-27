package com.ptit.ezychat.callback;

public interface LoginCallback {
    void onLoginSuccess();

    void onLoginError(String error);
}
