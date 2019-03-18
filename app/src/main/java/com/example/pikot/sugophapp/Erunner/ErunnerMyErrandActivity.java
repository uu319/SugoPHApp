package com.example.pikot.sugophapp.Erunner;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.Both.ChatApp;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.Eseeker.EseekerErrandPaymentDetails;
import com.example.pikot.sugophapp.Eseeker.EseekerPostDocumentActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.DateHandler;
import com.example.pikot.sugophapp.RandomClasses.Errands;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.AcceptErrandRequest;
import com.example.pikot.sugophapp.Requests.CancelDenyRequest;
import com.example.pikot.sugophapp.Requests.CompleteErrandRequest;
import com.example.pikot.sugophapp.Requests.DenyErrandRequest;
import com.example.pikot.sugophapp.Requests.GetMatchErunnerExceptRequest;
import com.example.pikot.sugophapp.Requests.GetUserErrandByErrandIdRequest;
import com.example.pikot.sugophapp.Requests.StartErrandRequest;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ErunnerMyErrandActivity extends AppCompatActivity implements View.OnClickListener{
    private BroadcastReceiver receiver;

    Errands errands;
    ArrayList<Errands> errandArrayList;
    Intent intent;
    String errand_id;
    Button btnDeny, btnAccept, btnToggle;
    TextView txtStatus, txtAgo, txtPayment, txtName, txtAddress, txtOptionName, txtDatePublished, txtDescription1, txtDescription2, txtCurrentLocation;
    CircleImageView imgProfpic;

    SpeedDialView speedDialView;


//    FloatingActionButton fabMessage;

    LinearLayout linearLayoutProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erunner_my_errand);
        intent= getIntent();
        errand_id= intent.getStringExtra("errand_id");


        txtStatus= findViewById(R.id.txtStatus);
        txtAgo= findViewById(R.id.txtAgo);
        txtPayment= findViewById(R.id.txtPayment);
        txtName= findViewById(R.id.txtName);
//        txtAddress= findViewById(R.id.txtAddress);
        txtOptionName= findViewById(R.id.txtOptionName);
        txtDatePublished= findViewById(R.id.txtDatePublished);
        txtDescription1= findViewById(R.id.txtDescription1);
        txtDescription2= findViewById(R.id.txtDescription2);
        linearLayoutProfile= findViewById(R.id.layoutProfile);
        imgProfpic= findViewById(R.id.imgProfpic);
        btnAccept= findViewById(R.id.btnAccept);
        btnDeny= findViewById(R.id.btnDeny);
        btnToggle= findViewById(R.id.btnToggle);
//        fabMessage= findViewById(R.id.fabMessage);
        txtCurrentLocation= findViewById(R.id.txtCurrentLocation);

        speedDialView = findViewById(R.id.speedDial);


        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_message, R.drawable.fab_message).setLabel("Message")
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_map, R.drawable.fab_map).setLabel("Map")
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.hide();





    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                  recreate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        LocalBroadcastManager.getInstance(ErunnerMyErrandActivity.this).registerReceiver((receiver), new IntentFilter("errand"));
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
        try{unregisterReceiver(receiver);}
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.btnAccept:
//                break;
//            case R.id.btnDeny:
//                break;
//            case R.id.btnToggle:
//                break;
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void getDetails(){
        GetUserErrandByErrandIdRequest.getUserErrandByErrandId(this, errand_id, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("msg");
                    final JSONObject errandDetails= jsonArray.getJSONObject(0);
                    JSONObject erunnerDetails= jsonArray.getJSONObject(1);

                    final String latlong= errandDetails.getString("location");
                    final String [] locArray= latlong.split(",");
                    final Geocoder geocoder = new Geocoder(ErunnerMyErrandActivity.this, Locale.getDefault());
                    String address1="";
                    try {
                        List<Address> addressList= geocoder.getFromLocation(Double.valueOf(locArray[0]),Double.valueOf(locArray[1]),1);
                        Log.d("Address", addressList.toString());
                        address1=addressList.get(0).getAddressLine(0);
                        txtCurrentLocation.setText(address1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    String status = errandDetails.getString("status");
//                    String description = errandDetails.getString("description");
//                    String[] stringArray = description.split(",");
                    String name = erunnerDetails.getString("firstname") + " " + erunnerDetails.getString("middlename") + " " + erunnerDetails.getString("lastname");
                    String address = erunnerDetails.getString("street") + ", " + erunnerDetails.getString("barangay") + ", " + erunnerDetails.getString("city");
                    if(jsonObject.getBoolean("error")){
                        try{
                            ShowAlert.showAlert(ErunnerMyErrandActivity.this,jsonObject.getString("msg"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        Toast.makeText(ErunnerMyErrandActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }else {
                        txtStatus.setText(jsonArray.getJSONObject(0).getString("status"));
                        Glide.with(getApplicationContext()).load(Constants.URL_GET_IMAGE + erunnerDetails.getString("username") + ".jpg").into(imgProfpic);
                        txtName.setText(name);
//                        txtAddress.setText(address);
                        if(status.equals("Waiting for accept")){
                            Log.d("Status", status);
                            btnAccept.getLayoutParams().height=LinearLayout.LayoutParams.WRAP_CONTENT;
                            btnDeny.getLayoutParams().height=LinearLayout.LayoutParams.WRAP_CONTENT;
                            btnAccept.setVisibility(View.VISIBLE);
                            btnDeny.setVisibility(View.VISIBLE);
                            btnAccept.setEnabled(true);
                            btnAccept.setText("Accept");
                            btnDeny.setEnabled(true);
                            btnDeny.setText("Deny");
                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AcceptErrandRequest.acceptErrand(ErunnerMyErrandActivity.this, errand_id, new CallBacks() {
                                        @Override
                                        public void onSuccess(String response) throws JSONException {
                                            if(response!=null){
                                                JSONObject jsonObject1= new JSONObject(response);
                                                if(jsonObject1.getBoolean("error")){
                                                    try{
                                                        ShowAlert.showAlert(ErunnerMyErrandActivity.this,jsonObject1.getString("msg"));
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }else{

                                                    recreate();
                                                }
                                            }else{
                                                ShowAlert.showAlert(ErunnerMyErrandActivity.this,"Something went wrong.");
                                            }
                                        }
                                    });
                                }
                            });
                            btnDeny.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DenyErrandRequest.denyErrand(ErunnerMyErrandActivity.this, errand_id, new CallBacks() {
                                        @Override
                                        public void onSuccess(String response) throws JSONException {
                                            if(response!=null){
                                                JSONObject jsonObject1= new JSONObject(response);
                                                if(jsonObject1.getBoolean("error")){
                                                    try{
                                                        ShowAlert.showAlert(ErunnerMyErrandActivity.this,jsonObject1.getString("msg"));
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }else {
                                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ErunnerMyErrandActivity.this);
                                                    alertDialog.setTitle("Alert");
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.setMessage("Are you sure you want to deny this errand?");
                                                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            CancelDenyRequest.cancelErrand(ErunnerMyErrandActivity.this, errand_id, new CallBacks() {
                                                                @Override
                                                                public void onSuccess(String response) throws JSONException {
                                                                    if(response!=null){
                                                                        JSONObject jsonObjectCancel= new JSONObject(response);
                                                                        if(jsonObjectCancel.getBoolean("error")){
                                                                            ShowAlert.showAlert(ErunnerMyErrandActivity.this,jsonObjectCancel.getString("msg"));
                                                                        }else{
                                                                            recreate();
                                                                        }
                                                                    }else{
                                                                        ShowAlert.showAlert(ErunnerMyErrandActivity.this,"Something went wrong.");
                                                                    }
                                                                }
                                                            });
//
                                                        }


                                                    });
                                                    alertDialog.setPositiveButton("Deny", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            GetMatchErunnerExceptRequest.getMatchRequest(ErunnerMyErrandActivity.this, errand_id, new CallBacks() {
                                                                @Override
                                                                public void onSuccess(String response) throws JSONException {

                                                                }
                                                            });
                                                            SharedPrefManager.getInstance(ErunnerMyErrandActivity.this).setKeyStatus("active");
                                                            startActivity(new Intent(ErunnerMyErrandActivity.this, MainActivity.class));
                                                        }
                                                    });
                                                    alertDialog.show();
                                                }
                                            }else{
                                                ShowAlert.showAlert(ErunnerMyErrandActivity.this,"Something went wrong.");
                                            }
                                        }
                                    });

                                }
                            });
                        }else if(status.equals("Accepted")){
                            speedDialView.show();
                            btnAccept.getLayoutParams().height=1;
                            btnDeny.getLayoutParams().height=1;
                            btnToggle.setVisibility(View.VISIBLE);
                            btnToggle.setText("Start");
                            btnToggle.getLayoutParams().height=LinearLayout.LayoutParams.WRAP_CONTENT;
                            btnToggle.setEnabled(true);
                            btnToggle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    StartErrandRequest.startErrand(ErunnerMyErrandActivity.this, errand_id, new CallBacks() {
                                        @Override
                                        public void onSuccess(String response) throws JSONException {
                                            if(response!=null){
                                                JSONObject jsonObject1= new JSONObject(response);
                                                if(jsonObject1.getBoolean("error")){
                                                    try{
                                                        ShowAlert.showAlert(ErunnerMyErrandActivity.this,jsonObject1.getString("msg"));
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }

//                                                    Toast.makeText(ErunnerMyErrandActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                                }else{
//
                                                    recreate();
                                                }
                                            }else{
                                                ShowAlert.showAlert(ErunnerMyErrandActivity.this,"Something went wrong.");
//
                                            }
                                        }
                                    });
                                }
                            });

                        }else if (status.equals("On-going")) {
                            speedDialView.show();
                            btnAccept.getLayoutParams().height=1;
                            btnDeny.getLayoutParams().height=1;
                            String ratePerHour= errandDetails.getString("rate_per_hour");
                            String bookingFee= errandDetails.getString("booking_fee");
                            String dateStarted= errandDetails.getString("date_start");
                            updateTotalPayment(dateStarted,ratePerHour,bookingFee);
                            try {
                                updateTimeAgo(dateStarted, txtAgo, "");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            final String total_fee=getTotalPayment(dateStarted,ratePerHour,bookingFee);
                            btnToggle.setText("Complete");
                            btnToggle.setVisibility(View.VISIBLE);
                            btnToggle.setEnabled(true);
                            btnToggle.getLayoutParams().height=LinearLayout.LayoutParams.WRAP_CONTENT;
                            btnToggle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CompleteErrandRequest.completeErrand(ErunnerMyErrandActivity.this, errand_id, total_fee, new CallBacks() {
                                        @Override
                                        public void onSuccess(String response) throws JSONException {
                                            if(response!=null){
                                                JSONObject jsonObject4= new JSONObject(response);
                                                if(jsonObject4.getBoolean("error")){
                                                    try{
                                                        ShowAlert.showAlert(ErunnerMyErrandActivity.this,jsonObject4.getString("msg"));
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }

//                                                    Toast.makeText(ErunnerMyErrandActivity.this, jsonObject4.getString("msg"), Toast.LENGTH_SHORT).show();
                                                }else{
                                                    SharedPrefManager.getInstance(ErunnerMyErrandActivity.this).setKeyStatus("active");
                                                    recreate();
                                                }
                                            }else{
                                                ShowAlert.showAlert(ErunnerMyErrandActivity.this,"Something went wrong.");
//                                                Toast.makeText(ErunnerMyErrandActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }else if(status.equals("Completed")){
                            speedDialView.show();
                            btnAccept.getLayoutParams().height=1;
                            btnDeny.getLayoutParams().height=1;
                            String dateEnd= errandDetails.getString("date_end");
                            String total_fee= errandDetails.getString("total_fee");
                            txtPayment.setText(total_fee);
                            try {
                                updateTimeAgo(dateEnd, txtAgo, "");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else if(status.equals("Confirmed")){
                            String dateEnd= errandDetails.getString("date_end");
                            String total_fee= errandDetails.getString("total_fee");
                            txtPayment.setText(total_fee);
                            try {
                                updateTimeAgo(dateEnd, txtAgo, "");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
                            @Override
                            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                                switch (speedDialActionItem.getId()) {
                                    case R.id.fab_message:
                                        Intent intent= new Intent(ErunnerMyErrandActivity.this, ChatApp.class);
                                        intent.putExtra("errand_id", errand_id);
                                        startActivity(intent);
                                        return false; // true to keep the Speed Dial open
                                    case R.id.fab_map:
                                        String address1="";
                                        try {
                                            List<Address> addressList= geocoder.getFromLocation(Double.valueOf(locArray[0]),Double.valueOf(locArray[1]),1);
                                            Log.d("Address", addressList.toString());
                                            address1=addressList.get(0).getAddressLine(0);
                                            txtCurrentLocation.setText(address1);
                                            startActivity(new Intent(ErunnerMyErrandActivity.this, ErunnerMapsActivity.class)
                                                    .putExtra("lat",Double.valueOf(locArray[0])).putExtra("long",Double.valueOf(locArray[1]))
                                                    .putExtra("type","erunner"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        return false;
                                    default:
                                        return false;
                                }
                            }
                        });

                        try {
                            updateTimeAgo(errandDetails.getString("date_published"),txtDatePublished, "Published");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                                   txtOptionName.setText(errandDetails.getString("option_name"));

                  

                        String description = jsonObject.getString("description");
                        String description2 = jsonObject.getString("additional_description");

                        txtDescription1.setText(description);
                        txtDescription2.setText(description2);
                }

                }else{
                    ShowAlert.showAlert(ErunnerMyErrandActivity.this, "Something went wrong with your connection.");
//                    Toast.makeText(ErunnerMyErrandActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateTimeAgo(final String givenDate, final TextView textView, final String label) throws ParseException {
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



    private  void updateTotalPayment(final String dateStarted, final String ratePerHour, final String bookingFee){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                        long timeStartInMilliseconds;
                        long timeNowInMilliseconds;
                        Date startDate= null;
                        Date nowDate= new Date();
                        try {
                            startDate = dateFormat.parse(dateStarted);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        timeStartInMilliseconds = startDate.getTime();
                        timeNowInMilliseconds = nowDate.getTime();
                        long diff= timeNowInMilliseconds-timeStartInMilliseconds;
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        long days = hours / 24;
                        long minutes_15=minutes/15;
                        long total_payment= minutes_15*(Integer.parseInt(ratePerHour)/4)+Integer.parseInt(bookingFee);
                        txtPayment.setText(total_payment+"php");
                    }
                });
            }
        },              0, DateUtils.MINUTE_IN_MILLIS*15);//1000 is a Refreshing Time (1second)
    }

    private String getTotalPayment(final String dateStarted, final String ratePerHour, final String bookingFee){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        long timeStartInMilliseconds;
        long timeNowInMilliseconds;
        Date startDate= null;
        Date nowDate= new Date();
        try {
            startDate = dateFormat.parse(dateStarted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeStartInMilliseconds = startDate.getTime();
        timeNowInMilliseconds = nowDate.getTime();
        long diff= timeNowInMilliseconds-timeStartInMilliseconds;
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long minutes_15=minutes/15;
        long total_payment= minutes_15*(Integer.parseInt(ratePerHour)/4)+Integer.parseInt(bookingFee);
        return total_payment+"";
    }


    private String convertDateToString(String date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date datePublished= null;
        try {
            datePublished= dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datePublished+"";
    }
}
