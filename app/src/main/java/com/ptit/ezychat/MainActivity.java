package com.ptit.ezychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ptit.ezychat.callback.LoginCallback;
import com.ptit.ezychat.socket.SocketClientProxy;
import com.ptit.ezychat.socket.SocketRequests;


public class MainActivity extends AppCompatActivity implements LoginCallback {
    private EditText username,password;
    private Button loginButton, registryButton;
    private ImageButton googleLogin;
    private WebView webView;
    private RelativeLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SocketClientProxy client = SocketClientProxy.getInstance();
        client.setLoginCallback(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
            }
        });

        registryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegistryRequest();
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registryButton.setVisibility(View.GONE);
                webView.loadUrl("https://ezy-chat-http-login.herokuapp.com/");
                webView.setVisibility(View.VISIBLE);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.toString().contains("getGoogleLoginToken")){
                    String baseUrl = "https://ezy-chat-http-login.herokuapp.com/getGoogleLoginToken/token=";
                    int any_delay_in_ms = 3000; //1Second interval
                    new Handler().postDelayed(() -> {
                        String token = url.substring(baseUrl.length());
                        onGoogleLoginSuccess(token);
                    }, any_delay_in_ms);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.setVisibility(View.GONE);
    }

    private  void  initView(){
        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        registryButton = findViewById(R.id.registryButton);
        googleLogin = findViewById(R.id.googleLogin);
        loading = findViewById(R.id.loading);
        webView = findViewById(R.id.wv);
        webView.getSettings().setUserAgentString("Chrome/56.0.0.0 Mobile");
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void sendLoginRequest(){
        SocketClientProxy.getInstance().login(username.getText().toString(),password.getText().toString(),0);
    }

    private void sendRegistryRequest(){
        SocketClientProxy.getInstance().login(username.getText().toString(),password.getText().toString(),1);
    }

    @Override
    public void onLoginSuccess() {
        loading.setVisibility(View.VISIBLE);
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginError(String error) {
        Toast.makeText(this,error+"! Try again!!!", Toast.LENGTH_SHORT).show();
    }

    public void onGoogleLoginSuccess(String token) {
        webView.setVisibility(View.GONE);
        registryButton.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        String username = "user#"+token.substring(0,6);
        String password = token.substring(0,15);
        SocketClientProxy.getInstance().login(username,password,0);
    }
}