package com.example.pikot.sugophapp.Both;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Chat;
import com.example.pikot.sugophapp.RandomClasses.ChatAdapter;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetMessagesRequest;
import com.example.pikot.sugophapp.Requests.SendMessageRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ChatApp extends AppCompatActivity {
    BroadcastReceiver receiver;
    ListView listMessages;
    ImageView imgSend;
    EditText editMessage;

    String errand_id;
    String test;

    ArrayList<Chat> chats;
    ChatAdapter chatAdapter;

    SwipeRefreshLayout pullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app);

        errand_id= getIntent().getStringExtra("errand_id");
        chats= new ArrayList<>();

        listMessages= findViewById(R.id.listMessages);
        listMessages.setStackFromBottom(true);
        imgSend= findViewById(R.id.imgSend);
        editMessage= findViewById(R.id.editMessage);
        pullToRefresh= findViewById(R.id.pullToRefresh);



        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                editMessage.setText("");
                sendMessage(editMessage.getText().toString());
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessages();
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            getMessages();
        }catch (Exception e){
            e.printStackTrace();
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                try {
                    if(intent.getStringExtra("errand_id")!=null){
                        String message = intent.getStringExtra("message");
                        chats.add(new Chat(true, message, new Date().toString()));
                        chatAdapter= new ChatAdapter(ChatApp.this, chats);
                        listMessages.setAdapter(chatAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("message")
        );

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void sendMessage(final String message){
        SendMessageRequest.sendMessage(this, errand_id,message, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error")){
                        Toast.makeText(ChatApp.this, "Message sending failed.", Toast.LENGTH_SHORT).show();
                    }else{
                        editMessage.setText("");
                        chats.add(new Chat(false, message, new Date().toString()));
                        chatAdapter= new ChatAdapter(ChatApp.this, chats);
                        listMessages.setAdapter(chatAdapter);
                    }
                }
            }
        });
    }

    private void getMessages(){
        try{chats.clear();}catch (Exception e){
            e.printStackTrace();
        }
        GetMessagesRequest.getMessages(this, errand_id, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error" )){
                        try {
                            ShowAlert.showAlert(ChatApp.this,"Error fetching message");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        Toast.makeText(ChatApp.this, "Error fetching messages.", Toast.LENGTH_SHORT).show();
                    }else{
                        JSONArray jsonArray= jsonObject.getJSONArray("msg");
                        Boolean isLeft;
                        for(int i=0;i<jsonArray.length();i++){

                            if(jsonArray.getJSONObject(i).getString("sender").equals(SharedPrefManager.getInstance(ChatApp.this).getKeyUsername())){
                                isLeft= false;
                            }else{
                                isLeft= true;
                            }
                           chats.add(new Chat(isLeft, jsonArray.getJSONObject(i).getString("message"), jsonArray.getJSONObject(i).getString("date")));
                        }
                        chatAdapter= new ChatAdapter(ChatApp.this, chats);
                        listMessages.setAdapter(chatAdapter);




                    }
                }else{
                    ShowAlert.showAlert(ChatApp.this,"Something went wrong.");
//                    Toast.makeText(ChatApp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



}
