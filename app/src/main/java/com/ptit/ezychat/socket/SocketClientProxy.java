package com.ptit.ezychat.socket;

import com.ptit.ezychat.callback.AddContactsCallBack;
import com.ptit.ezychat.callback.GetContactsCallBack;
import com.ptit.ezychat.callback.GetMessagesOfChannelCallBack;
import com.ptit.ezychat.callback.GetOnlineContactsCallBack;
import com.ptit.ezychat.callback.LoginCallback;
import com.ptit.ezychat.callback.SearchNewUsersCallback;
import com.ptit.ezychat.callback.SendMessageCallBack;
import com.ptit.ezychat.constant.Commands;
import com.ptit.ezychat.data.ChatChannelUsers;
import com.ptit.ezychat.model.Message;
import com.ptit.ezychat.model.User;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyClients;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.entity.EzyApp;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyArrayList;
import com.tvd12.ezyfoxserver.client.entity.EzyData;
import com.tvd12.ezyfoxserver.client.entity.EzyObject;
import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.client.handler.EzyAppAccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionFailureHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionSuccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyHandshakeHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyLoginErrorHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyLoginSuccessHandler;
import com.tvd12.ezyfoxserver.client.request.EzyAppAccessRequest;
import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.setup.EzyAppSetup;
import com.tvd12.ezyfoxserver.client.setup.EzySetup;

import java.util.ArrayList;
import java.util.List;

public class SocketClientProxy {

    private static final String ZONE_NAME = "EzyChat";
    private static final String APP_NAME = "EzyChat";


    private EzyClient client = setup();

    public EzyClient getClient() {
        return client;
    }

    private SocketClientProxy(){}

    private static SocketClientProxy INSTANCE = new SocketClientProxy();

    public static SocketClientProxy getInstance() {
        return INSTANCE;
    }


    private User userAuthenInfo = new User("","");
    public User getUserAuthenInfo() {
        return userAuthenInfo;
    }
    public void setUserAuthenInfo(User userAuthenInfo) {
        this.userAuthenInfo = userAuthenInfo;
    }

    private EzyObject isRegistry;

    public EzyObject getIsRegistry() {
        return isRegistry;
    }

    public void setIsRegistry(EzyObject isRegistry) {
        this.isRegistry = isRegistry;
    }

    private LoginCallback actionLogin;
    public  void setLoginCallback(LoginCallback actionLogin){
        this.actionLogin = actionLogin;
    }

    private SearchNewUsersCallback actionSearchNewUsers;
    public void setActionSearchNewUsers(SearchNewUsersCallback actionSearchNewUsers) {
        this.actionSearchNewUsers = actionSearchNewUsers;
    }

    private GetContactsCallBack actionGetContacts;
    public void setActionGetContacts(GetContactsCallBack actionGetContacts) {
        this.actionGetContacts = actionGetContacts;
    }

    private GetOnlineContactsCallBack actionGetOnlineContacts;
    public void setActionGetOnlineContacts(GetOnlineContactsCallBack actionGetOnlineContacts) {
        this.actionGetOnlineContacts = actionGetOnlineContacts;
    }

    private AddContactsCallBack actionAddContacts;
    public void setActionAddContacts(AddContactsCallBack actionAddContacts) {
        this.actionAddContacts = actionAddContacts;
    }

    private GetMessagesOfChannelCallBack actionGetMessagesOfChannel;
    public void setActionGetMessagesOfChannel(GetMessagesOfChannelCallBack actionGetMessagesOfChannel) {
        this.actionGetMessagesOfChannel = actionGetMessagesOfChannel;
    }

    private SendMessageCallBack actionSendMessage;
    public void setActionSendMessage(SendMessageCallBack actionSendMessage) {
        this.actionSendMessage = actionSendMessage;
    }

    protected EzyClient setup() {
        EzyClientConfig clientConfig = EzyClientConfig.builder()
                .zoneName(ZONE_NAME)
                .enableSSL()
                .enableDebug()
                .build();
        EzyClients clients = EzyClients.getInstance();
        client = clients.newClient(clientConfig);
        clients.addClient(client);
        EzySetup setup = client.setup();
        setup.addEventHandler(EzyEventType.CONNECTION_SUCCESS, new EzyConnectionSuccessHandler());
        setup.addEventHandler(EzyEventType.CONNECTION_FAILURE, new EzyConnectionFailureHandler());
        setup.addDataHandler(EzyCommand.HANDSHAKE, new ExHandshakeHandler());
        setup.addDataHandler(EzyCommand.LOGIN, new ExLoginSuccessHandler());
        setup.addDataHandler(EzyCommand.LOGIN_ERROR, new ExLoginErrorHandler());
        setup.addDataHandler(EzyCommand.APP_ACCESS, new ExAccessAppHandler());


        EzyAppSetup appSetup = setup.setupApp(APP_NAME);
        appSetup.addDataHandler(Commands.ADD_CONTACTS, new AddContactsResponseHandler());
        appSetup.addDataHandler(Commands.CHAT_GET_CONTACTS, new ContactsResponseHandler());
        appSetup.addDataHandler(Commands.CHAT_GET_ONLINE_CONTACTS, new OnlineContactsResponseHandler());
        appSetup.addDataHandler(Commands.SEARCH_NEW_CONTACTS, new NewContactsResponseHandler());
        appSetup.addDataHandler(Commands.CHAT_USER_MESSAGE, new SendMessageResponseHandler());
        appSetup.addDataHandler(Commands.CHAT_CHANNEL_MESSAGE, new GetMessagesOfChannelResponseHandler());
        appSetup.addDataHandler(Commands.CHAT_CHANNEL_STATUS, new GetChannelStatusResponseHandler());
        return client;
    }

    static class ExHandshakeHandler extends EzyHandshakeHandler{
        @Override
        protected EzyRequest getLoginRequest() {
            System.out.println("Gui login");
            String username = SocketClientProxy.getInstance().getUserAuthenInfo().getUsername();
            String password = SocketClientProxy.getInstance().getUserAuthenInfo().getPassword();
            EzyObject data = SocketClientProxy.getInstance().getIsRegistry();
            return new EzyLoginRequest(ZONE_NAME, username, password,data);
        }
    }


    static class ExLoginSuccessHandler extends EzyLoginSuccessHandler {
        @Override
        protected void handleLoginSuccess(EzyData responseData) {
            System.out.println("login thanh cong");
            client.send(new EzyAppAccessRequest(APP_NAME));
        }
    }

    static class ExLoginErrorHandler extends EzyLoginErrorHandler {
        @Override
        protected void handleLoginError(EzyArray data) {
            String error = data.get(1).toString().toUpperCase();
            getInstance().actionLogin.onLoginError(error);
        }
    }

    static class ExAccessAppHandler extends EzyAppAccessHandler{
        @Override
        protected void postHandle(EzyApp app, EzyArray data) {
            getInstance().actionLogin.onLoginSuccess();
        }
    }
    static class AddContactsResponseHandler implements EzyAppDataHandler<EzyObject> {
        @Override
        public void handle(EzyApp app, EzyObject data) {
            System.out.println("Nhan list added contacts");
            ChatChannelUsers chatChannelUsers = new ChatChannelUsers();
            chatChannelUsers.setChannelId(Long.parseLong(data.get("channelId").toString()));
            EzyArrayList usersList = data.get("users");
            List<String> users = new ArrayList<>();
            for(int j =0;j< usersList.size();j++){
                users.add(usersList.get(j));
            }
            chatChannelUsers.setUsers(users);
            System.out.println(chatChannelUsers.toString());
            getInstance().actionAddContacts.onAddContactsSuccess(chatChannelUsers);
        }
    }
    static class ContactsResponseHandler implements EzyAppDataHandler<EzyArray> {
        @Override
        public void handle(EzyApp app, EzyArray data) {
            System.out.println("Nhan list contacts");
            List<ChatChannelUsers> channelUsersList = new ArrayList<>();
            for(int i = 0;i< data.size();i++){
                EzyObject s = data.get(i);
                ChatChannelUsers channelUsers = new ChatChannelUsers();
                channelUsers.setChannelId(Long.parseLong(s.get("channelId").toString()));
                EzyArrayList usersList = s.get("users");
                List<String> users = new ArrayList<>();
                for(int j =0;j< usersList.size();j++){
                    users.add(usersList.get(j));
                }
                channelUsers.setUsers(users);
                channelUsersList.add(channelUsers);
            }

            getInstance().actionGetContacts.onGetContactsSuccess(channelUsersList);
        }
    }
    static class OnlineContactsResponseHandler implements EzyAppDataHandler<EzyArray> {
        @Override
        public void handle(EzyApp app, EzyArray data) {
            System.out.println("Nhan list online contacts");
            List<ChatChannelUsers> channelUsersList = new ArrayList<>();
            for(int i = 0;i< data.size();i++){
                EzyObject s = data.get(i);
                ChatChannelUsers channelUsers = new ChatChannelUsers();
                channelUsers.setChannelId(Long.parseLong(s.get("channelId").toString()));
                EzyArrayList usersList = s.get("users");
                List<String> users = new ArrayList<>();
                for(int j =0;j< usersList.size();j++){
                    users.add(usersList.get(j));
                }
                channelUsers.setUsers(users);
                channelUsersList.add(channelUsers);
            }

            getInstance().actionGetOnlineContacts.onGetOnlineContactsSuccess(channelUsersList);
        }
    }
    static class NewContactsResponseHandler implements EzyAppDataHandler<EzyArray> {
        @Override
        public void handle(EzyApp app, EzyArray data) {
            System.out.println("Nhan list new contacts");
            List<String> newSeachUsers = data.toList();
            getInstance().actionSearchNewUsers.onSearchSuccess(newSeachUsers);
        }
    }
    static class SendMessageResponseHandler implements EzyAppDataHandler<EzyObject> {
        @Override
        public void handle(EzyApp app, EzyObject data) {
            Message message = new Message();

            message.setId(Long.parseLong(data.get("id").toString()));
            message.setMessage(data.get("message"));
            message.setTime(data.get("time"));
            message.setChannelId(Long.parseLong(data.get("channelId").toString()));
            message.setSender(data.get("sender"));
            getInstance().actionSendMessage.onSendMessageSuccess(message);
        }
    }

    static  class GetMessagesOfChannelResponseHandler implements EzyAppDataHandler<EzyArray> {
        @Override
        public void handle(EzyApp app, EzyArray data) {
            List<Message> messageList = new ArrayList<>();
            for(int i = 0;i< data.size();i++){
                EzyObject object = data.get(i);

                Message message = new Message();

                message.setId(Long.parseLong(object.get("id").toString()));
                message.setMessage(object.get("message"));
                message.setTime(object.get("time"));
                message.setChannelId(Long.parseLong(object.get("channelId").toString()));
                message.setSender(object.get("sender"));

                messageList.add(message);
            }
            getInstance().actionGetMessagesOfChannel.onGetMessagesOfChannelSuccess(messageList);
        }
    }

    static  class GetChannelStatusResponseHandler implements EzyAppDataHandler<EzyObject> {
        @Override
        public void handle(EzyApp app, EzyObject data) {
            int status = Integer.parseInt(data.get("status").toString());
            getInstance().actionGetMessagesOfChannel.onGetChannelStatus(status);
        }
    }

    public void login(String username, String password, int isReg)
    {
        setUserAuthenInfo(new User(username,password));
         this.isRegistry = EzyEntityFactory
                .newObjectBuilder()
                .append("isReg", isReg)
                .build();
        String host = "192.168.43.20";
        if (client.isConnected()) {
            client.disconnect();
        }
        else {

            client.connect(host, 3005);
        }
    }

}
