package com.ptit.ezychat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ptit.ezychat.adapter.MessageAdapter;
import com.ptit.ezychat.cache_db.SQLHelper;
import com.ptit.ezychat.callback.GetMessagesOfChannelCallBack;
import com.ptit.ezychat.callback.SendMessageCallBack;
import com.ptit.ezychat.constant.Commands;
import com.ptit.ezychat.data.ChatChannelUsers;
import com.ptit.ezychat.model.Message;
import com.ptit.ezychat.service.WordMatchingService;
import com.ptit.ezychat.socket.SocketClientProxy;
import com.ptit.ezychat.socket.SocketRequests;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageActivity extends AppCompatActivity implements GetMessagesOfChannelCallBack, SendMessageCallBack {

    ImageButton back, send;
    EditText messageInput;
    TextView targetName, status;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    ChatChannelUsers chatChannelUsers;
    List<Message> liveMessagesList;
    private int isPlaying;
    private String lastMessage;
    private Message currentMessage;
    private List<String> playedWords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        chatChannelUsers= (ChatChannelUsers) getIntent().getSerializableExtra("chatChannelUsers");

        SocketClientProxy.getInstance().setActionGetMessagesOfChannel(this);
        SocketClientProxy.getInstance().setActionSendMessage(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                if(!message.equals("")){
                    Message chatMessage = new Message();
                    chatMessage.setMessage(message);
                    chatMessage.setChannelId(chatChannelUsers.getChannelId());
                    chatMessage.setSender(getYourName());
                    sendMessage(chatMessage);
                    messageInput.setText("");
                }
            }
        });

    }

    private void initView(){
        back = findViewById(R.id.back);
        send = findViewById(R.id.sendButton);
        targetName = findViewById(R.id.targetName);
        status = findViewById(R.id.status);
        messageInput = findViewById(R.id.messageInput);
        recyclerView = findViewById(R.id.messageList);
        liveMessagesList = new ArrayList<>();
        playedWords = new ArrayList<>();
    }

    private void initData(){
        targetName.setText(chatChannelUsers.getUsers().get(0));
        SQLHelper sqlHelper = new SQLHelper(getBaseContext());
        liveMessagesList = sqlHelper.searchMessagesByChannelId(chatChannelUsers.getChannelId());
        if(liveMessagesList.size()>0){
            getMessagesOfChannel(chatChannelUsers.getChannelId(), liveMessagesList.get(liveMessagesList.size()-1).getId());
        }
        else{
            getMessagesOfChannel(chatChannelUsers.getChannelId(), 0);
        }
        channelStatus(Commands.GAME_STATUS);
    }



    private void getMessagesOfChannel(long channelId, long maxId){
        SocketRequests.getINSTANCE().getMessagesOfChannel(channelId, maxId);
    }

    @Override
    public void onGetMessagesOfChannelSuccess(List<Message> messageList) {
        for(int i=0;i<messageList.size();i++){
            SQLHelper sqlHelper = new SQLHelper(getBaseContext());
            sqlHelper.addMessage(messageList.get(i));
            liveMessagesList.add(messageList.get(i));
            System.out.println(messageList.get(i).toString());

        }
        messageAdapter = new MessageAdapter(this, liveMessagesList);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);

    }

    private void channelStatus(int status){
        SocketRequests.getINSTANCE().channelStatus(chatChannelUsers.getChannelId(), status);
    }
    @Override
    public void onGetChannelStatus(int status) {
        this.isPlaying = status;
    }


    private void sendMessage(Message message){
        SocketRequests.getINSTANCE().sendMessage(message);
    }

    @Override
    public void onSendMessageSuccess(Message message) {
        SQLHelper sqlHelper = new SQLHelper(getBaseContext());
        sqlHelper.addMessage(message);
        String startStr = "Word Matching Game started!";
        String endStr = "Word Matching Game ended!";
        if(liveMessagesList.size()>0 && !liveMessagesList.get(liveMessagesList.size()-1).getMessage().equals("/end")){
            lastMessage = liveMessagesList.get(liveMessagesList.size()-1).getMessage();
        }
        liveMessagesList.add(message);

        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
        if(!message.getSender().equals(getYourName())) {
            if (message.getMessage().equals("/game") && isPlaying == 0) {
                sendStartGameCommand(message);
            }
            if (message.getMessage().equals("/end") && isPlaying == 1) {
                sendEndGameResult(message);
            }
            if (message.getMessage().contains(startStr) || message.getMessage().contains(endStr)) {
                channelStatus(Commands.GAME_STATUS);
            }
        }
        if(isPlaying==1 && !message.getMessage().equals("/end") && !message.getMessage().contains(startStr) && !message.getMessage().contains(endStr) ){
            onGamePlaying(message);
        }



    }

    private void sendStartGameCommand(Message message){
        channelStatus(Commands.GAME_PLAYING);

        Message startGameCommand = new Message();
        startGameCommand.setMessage("Word Matching Game started!      " + message.getSender() + " go first!" );
        startGameCommand.setChannelId(message.getChannelId());
        startGameCommand.setSender(getYourName());
        sendMessage(startGameCommand);

    }
    private void onGamePlaying(Message message){
        checkValidWord(message.getMessage());
        this.currentMessage = message;
    }

    private void sendEndGameCommand(Message message){
        Message endGameCommand = new Message();
        endGameCommand.setMessage("/end");
        endGameCommand.setChannelId(message.getChannelId());
        endGameCommand.setSender(getYourName());
        sendMessage(endGameCommand);
    }
    private void sendEndGameResult(Message message){
        channelStatus(Commands.GAME_ENDING);

        Message endGameResult = new Message();
        endGameResult.setMessage("Word Matching Game ended!      " + getYourName() +" won! "+ message.getSender() + " lose!" );
        endGameResult.setChannelId(message.getChannelId());
        endGameResult.setSender(getYourName());
        sendMessage(endGameResult);
    }

    private void checkValidWord(String word){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.dictionaryapi.dev")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WordMatchingService service = retrofit.create(WordMatchingService.class);
        Call<ResponseBody> repo = service.wordChecking(word);
        repo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==404){
                    onCheckValidWordResponse(false);
                }else{
                    onCheckValidWordResponse(true);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("fail: " +  t.toString());
            }
        });
    }

    private void onCheckValidWordResponse(boolean isValid){
        if(lastMessage.contains("Word Matching Game started!") ){    //1st word
            String newMessage = currentMessage.getMessage().toLowerCase();
            lastMessage = newMessage.substring(0,1);
        }

        if(currentMessage.getSender().equals(getYourName()) && (currentMessage.getMessage().toLowerCase().charAt(0)!= lastMessage.charAt(lastMessage.length()-1) || !isValid || playedWords.contains(currentMessage.getMessage()))){
            sendEndGameCommand(currentMessage);
            playedWords.clear();
            return;
        }
        playedWords.add(currentMessage.getMessage());
    }
    private String getYourName(){
        return SocketClientProxy.getInstance().getUserAuthenInfo().getUsername();
    }

}