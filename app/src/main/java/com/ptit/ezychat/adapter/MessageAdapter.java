package com.ptit.ezychat.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ptit.ezychat.R;
import com.ptit.ezychat.model.Message;
import com.ptit.ezychat.socket.SocketClientProxy;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Message> mList;
    private final Context context;
    private static final int sentMessage = 1;
    private static final int receivedMessage = 2;
    public MessageAdapter(Context context, List<Message> mList) {
        this.context = context;
        this.mList = mList;
    }
    public MessageAdapter(Context context) {
        this.context = context;
        mList= new ArrayList<>();
    }
    public void addItem( Message message){
        mList.add(message);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getSender().equals(SocketClientProxy.getInstance().getUserAuthenInfo().getUsername())){
            return sentMessage;
        }
        return receivedMessage;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == sentMessage){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_message_item_sent,parent,false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_message_item_received,parent,false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mList.get(position);
        if(message == null) return;
        holder.tMessage.setText(message.getMessage());
        holder.tTime.setText(message.getTime());


    }

    @Override
    public int getItemCount() {
        if(mList!=null) return mList.size();
        return 0;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        private TextView tMessage, tTime;
        public MessageViewHolder(@NonNull View view) {
            super(view);
            tMessage = view.findViewById(R.id.message);
            tTime = view.findViewById(R.id.time);

        }

    }

}
