package com.example.pikot.sugophapp.Eseeker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.DateHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.Requests.CancelErrandRequest;
import com.example.pikot.sugophapp.Requests.GetUserErrandByErrandIdRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class EseekerPendingErrand extends AppCompatActivity {
    TextView txtOptionName, txtDatePublished, txtStatus,txtDescription1,txtDescription2;
    Button btnCancel;
    String errand_id;

    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eseeker_pending_errand);
        txtOptionName= findViewById(R.id.txtOptionName);
        txtDatePublished= findViewById(R.id.txtDatePublished);
        txtStatus= findViewById(R.id.txtStatus);
        txtDescription1= findViewById(R.id.txtDescription1);
        txtDescription2= findViewById(R.id.txtDescription2);
        btnCancel= findViewById(R.id.btnToggle);

        try{
            errand_id= getIntent().getStringExtra("errand_id");
        }catch(Exception e){
            e.printStackTrace();
        }
//        Toast.makeText(this, errand_id, Toast.LENGTH_SHORT).show();


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelErrandRequest.cancelErrand(EseekerPendingErrand.this, errand_id, new CallBacks() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if (response != null) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.getStringExtra("errand_id") != null) {
                        startActivity(new Intent(getApplicationContext(), EseekerPendingErrand.class).putExtra("errand_id", errand_id));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("errand_deny")
        );
        try{
            getDetails();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{ unregisterReceiver(receiver);}catch(IllegalArgumentException e){
            e.printStackTrace();
        }

    }

    private void getDetails(){
        GetUserErrandByErrandIdRequest.getUserErrandByErrandId(this, getIntent().getStringExtra("errand_id"), new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null) {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(EseekerPendingErrand.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("msg");
                        final JSONObject errandDetails = jsonArray.getJSONObject(0);
                        String status = errandDetails.getString("status");
                        txtStatus.setText(status);
                        txtOptionName.setText(errandDetails.getString("option_name"));
                        String description = jsonObject.getString("description");
                        String description2 = jsonObject.getString("additional_description");
                        txtDescription1.setText(description);
                        txtDescription2.setText(description2);

                        try {
                            updateTimeAgo1(errandDetails.getString("date_published"), txtDatePublished, "Published");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                            }
                }
            }
        }
        );
    }

    private void updateTimeAgo1(final String givenDate, final TextView textView, final String label) throws ParseException {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String timeAgo="";
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                        long timeStartInMilliseconds;
                        long timeNowInMilliseconds;

                        Date startDate= null;
                        try {
                            startDate = dateFormat.parse(givenDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        timeStartInMilliseconds = startDate.getTime();


                        Date nowDate= new Date();
                        timeNowInMilliseconds = nowDate.getTime();
                        int days = DateHandler.daysBetween(startDate, nowDate);
                        int weeks= days/7;
                        int minutes = DateHandler.hoursBetween(startDate, nowDate);
                        int hours = minutes / 60;


                        if(minutes<60){
                            timeAgo= DateUtils.getRelativeTimeSpanString(timeStartInMilliseconds, timeNowInMilliseconds, DateUtils.MINUTE_IN_MILLIS,DateUtils.FORMAT_ABBREV_RELATIVE).toString();
                        }else if(minutes>60 && hours<24){
                            timeAgo= DateUtils.getRelativeTimeSpanString(timeStartInMilliseconds, timeNowInMilliseconds, DateUtils.HOUR_IN_MILLIS,DateUtils.FORMAT_ABBREV_RELATIVE).toString();
                        }else if(hours>24 && weeks<7){
                            timeAgo= DateUtils.getRelativeTimeSpanString(timeStartInMilliseconds, timeNowInMilliseconds, DateUtils.DAY_IN_MILLIS,DateUtils.FORMAT_ABBREV_RELATIVE).toString();
                        }
                        textView.setText(label+ " "+timeAgo);

                    };
                });
            }
        },              0, DateUtils.MINUTE_IN_MILLIS);//1000 is a Refreshing Time (1second)
    }
}

