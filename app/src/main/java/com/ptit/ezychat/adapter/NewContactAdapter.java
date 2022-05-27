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
import com.ptit.ezychat.model.Message;

import java.util.List;

public class NewContactAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> contactsList;

    public NewContactAdapter(@NonNull Context context, List<String> contactsList) {
        super(context, R.layout.component_contact_list_item, contactsList);
        this.context = context;
        this.contactsList = contactsList;
    }

    public void removeItem( String contact){
        contactsList.remove(contact);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.component_contact_list_item, null, true);
        TextView username = view.findViewById(R.id.username);
        String user = contactsList.get(position);
        username.setText(user);

        return view;
    }
    public String getItem(int pos){
        return contactsList.get(pos);
    }
}
