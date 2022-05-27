package com.ptit.ezychat.callback;

import java.util.List;

public interface SearchNewUsersCallback {
    void onSearchSuccess(List<String> newSeachUsers);
}
