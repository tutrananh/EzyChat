package com.ptit.ezychat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ptit.ezychat.MessageActivity;
import com.ptit.ezychat.R;
import com.ptit.ezychat.adapter.NewContactAdapter;
import com.ptit.ezychat.callback.AddContactsCallBack;
import com.ptit.ezychat.callback.SearchNewUsersCallback;
import com.ptit.ezychat.data.ChatChannelUsers;
import com.ptit.ezychat.socket.SocketClientProxy;
import com.ptit.ezychat.socket.SocketRequests;

import java.util.ArrayList;
import java.util.List;


public class SearchContactFragment extends Fragment implements SearchNewUsersCallback, AddContactsCallBack {
    ImageButton back, search;
    EditText keyword;
    ListView listView;
    NewContactAdapter newContactAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_search,container,false);
        back = view.findViewById(R.id.back);
        search = view.findViewById(R.id.search);
        keyword = view.findViewById(R.id.keyword);
        listView= view.findViewById(R.id.contactList);
        SocketClientProxy.getInstance().setActionSearchNewUsers(this);
        SocketClientProxy.getInstance().setActionAddContacts(this);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketRequests.getINSTANCE().sendSearchContacts(keyword.getText().toString());
                keyword.setText("");
            }
        });

        return view;
    }

    @Override
    public void onSearchSuccess(List<String> newSeachUsers) {
        newContactAdapter = new NewContactAdapter(getContext(), newSeachUsers);
        listView.setAdapter(newContactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String selection = newContactAdapter.getItem(pos);
                List<String> usernames = new ArrayList<>();
                usernames.add(selection);
                sendAddContacts(usernames);
                newContactAdapter.removeItem(selection);
            }
        });
    }

    @Override
    public void onAddContactsSuccess(ChatChannelUsers chatChannelUsers) {
        chat(chatChannelUsers);
    }

    private void sendAddContacts(List<String> usernames){
        SocketRequests.getINSTANCE().sendAddContacts(usernames);
    }
    private void chat(ChatChannelUsers chatChannelUsers){
        Intent intent = new Intent(getContext(), MessageActivity.class);
        intent.putExtra("chatChannelUsers", chatChannelUsers);
        startActivity(intent);
    }
}
