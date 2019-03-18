package com.example.pikot.sugophapp.RandomClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.ChatApp;
import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.Both.RegName;
import com.example.pikot.sugophapp.Erunner.ErunnerHistoryFragment;
import com.example.pikot.sugophapp.Erunner.ErunnerMyErrandActivity;
import com.example.pikot.sugophapp.Eseeker.EseekerErrandFullDetails;
import com.example.pikot.sugophapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService    extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("New Token: " ,"generated new token");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

          if(remoteMessage.getData().get("action").equals("message")){
              String channel_id= remoteMessage.getData().get("channel");

              String errand_id= remoteMessage.getData().get("errand_id");
              eseekerMessageNotification(remoteMessage, errand_id, channel_id);
              Intent intent= new Intent("message");
              intent.putExtra("message", remoteMessage.getData().get("message"));
              intent.putExtra("errand_id", remoteMessage.getData().get("errand_id"));
              LocalBroadcastManager broadcastManager= LocalBroadcastManager.getInstance(getBaseContext());
              broadcastManager.sendBroadcast(intent);
          }
        if(remoteMessage.getData().get("action").equals("eseeker_errand")){
            String channel_id= remoteMessage.getData().get("channel");
            String errand_id= remoteMessage.getData().get("errand_id");
            eseekerErrandNotification(remoteMessage, errand_id, channel_id);
                  Intent intent= new Intent("errand");
            intent.putExtra("errand_id", remoteMessage.getData().get("errand_id"));
            LocalBroadcastManager broadcastManager= LocalBroadcastManager.getInstance(getBaseContext());
            broadcastManager.sendBroadcast(intent);
        }

        if(remoteMessage.getData().get("action").equals("erunner_errand")){
            String channel_id= remoteMessage.getData().get("channel");
            if(channel_id.equals("match")){
                SharedPrefManager.getInstance(getBaseContext()).setKeyStatus("unavailable");
            }
            String errand_id= remoteMessage.getData().get("errand_id");
            erunnerErrandNotification(remoteMessage, errand_id, channel_id);
            Intent intent= new Intent("errand");
            intent.putExtra("errand_id", remoteMessage.getData().get("errand_id"));
            LocalBroadcastManager broadcastManager= LocalBroadcastManager.getInstance(getBaseContext());
            broadcastManager.sendBroadcast(intent);
        }

        if(remoteMessage.getData().get("action").equals("eseeker_err")){
            String channel_id= remoteMessage.getData().get("channel");
            String errand_id= remoteMessage.getData().get("errand_id");
            erunnerErrandNotification(remoteMessage, errand_id, channel_id);
            Intent intent= new Intent("errand_deny");
            intent.putExtra("errand_id", remoteMessage.getData().get("errand_id"));
            LocalBroadcastManager broadcastManager= LocalBroadcastManager.getInstance(getBaseContext());
            broadcastManager.sendBroadcast(intent);
        }
    }
    private void eseekerErrandNotification(RemoteMessage remoteMessage, String errand_id, String channelId){
        Intent intent = new Intent(this, EseekerErrandFullDetails.class);
        intent.putExtra("errand_id", errand_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("com.example.pikot.sugophapp", "SugoPh", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private void erunnerErrandNotification(RemoteMessage remoteMessage, String errand_id, String channelId){
        Intent intent = new Intent(this, ErunnerMyErrandActivity.class);
        intent.putExtra("errand_id", errand_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("com.example.pikot.sugophapp", "SugoPh", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private void eseekerMessageNotification(RemoteMessage remoteMessage, String errand_id, String channelId){
        Intent intent = new Intent(this, ChatApp.class);
        intent.putExtra("errand_id", errand_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notif_message)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("com.example.pikot.sugophapp", "SugoPh", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }
}
