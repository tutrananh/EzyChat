package com.ptit.ezychat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ptit.ezychat.MessageActivity;
import com.ptit.ezychat.R;
import com.ptit.ezychat.adapter.ContactAdapter;
import com.ptit.ezychat.callback.GetOnlineContactsCallBack;
import com.ptit.ezychat.data.ChatChannelUsers;
import com.ptit.ezychat.socket.SocketClientProxy;
import com.ptit.ezychat.socket.SocketRequests;

import java.util.ArrayList;
import java.util.List;

public class OnlineContactListFragment extends Fragment implements GetOnlineContactsCallBack {
    private ListView listView;
    private TextView username;
    private ContactAdapter contactAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_contact_list,container,false);
        listView = view.findViewById(R.id.contactList);
        username = view.findViewById(R.id.username);
        SocketClientProxy.getInstance().setActionGetOnlineContacts(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }
    private void initView() {
        username.setText(SocketClientProxy.getInstance().getUserAuthenInfo().getUsername());
    }

    @Override
    public void onGetOnlineContactsSuccess(List<ChatChannelUsers> contacts) {
        contactAdapter = new ContactAdapter(getContext(),contacts);
        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ChatChannelUsers selection = contactAdapter.getItem(pos);
                chat(selection);

            }
        });
    }
    private void chat(ChatChannelUsers chatChannelUsers){
        Intent intent = new Intent(getContext(), MessageActivity.class);
        intent.putExtra("chatChannelUsers", chatChannelUsers);
        startActivity(intent);
    }
}
