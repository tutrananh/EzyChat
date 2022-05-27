package com.ptit.ezychat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ptit.ezychat.R;
import com.ptit.ezychat.cache_db.SQLHelper;
import com.ptit.ezychat.data.ChatChannelUsers;
import com.ptit.ezychat.model.Message;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<ChatChannelUsers> {
    private Context context;
    private List<ChatChannelUsers> contactsList;

    public void setContactsList(List<ChatChannelUsers> contactsList) {
        this.contactsList = contactsList;
    }

    public ContactAdapter(@NonNull Context context, List<ChatChannelUsers> contactsList) {
        super(context, R.layout.component_contact_list_item, contactsList);
        this.context = context;
        this.contactsList = contactsList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.component_contact_list_item, null, true);
        TextView username = view.findViewById(R.id.username);
        TextView lastMessageText = view.findViewById(R.id.lastMessage);
        String user = getItem(position).getUsers().get(0);
        username.setText(user);

        SQLHelper sqlHelper = new SQLHelper(getContext());
        Message lastMessage = sqlHelper.searchLastMessageByChannelId(getItem(position).getChannelId());
        if(lastMessage.getMessage()!=null && lastMessage.getSender()!=null){
            lastMessageText.setText(lastMessage.getSender()+ ": " + lastMessage.getMessage());
        }

        return view;
    }
    public ChatChannelUsers getItem(int pos){
        return contactsList.get(pos);
    }

}
