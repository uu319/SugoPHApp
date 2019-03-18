package com.example.pikot.sugophapp.Eseeker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.Both.ChatApp;
import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.Erunner.ErunnerMapsActivity;
import com.example.pikot.sugophapp.Erunner.ErunnerMyErrandActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Chat;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.DateHandler;
import com.example.pikot.sugophapp.RandomClasses.PaypalConfig;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.RandomClasses.Validation;
import com.example.pikot.sugophapp.Requests.CancelErrandRequest;
import com.example.pikot.sugophapp.Requests.ConfirmErrandRequest;
import com.example.pikot.sugophapp.Requests.EvaluationRequest;
import com.example.pikot.sugophapp.Requests.GetMatchRequest;
import com.example.pikot.sugophapp.Requests.GetUserErrandByErrandIdRequest;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class EseekerErrandFullDetails extends AppCompatActivity {


    String errand_id;
    BroadcastReceiver receiver;
    String globalStatus;
    SpeedDialView speedDialView;

    public static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config= new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PaypalConfig.PAYPAL_CLIENT_IID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eseeker_errand_full_details);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }





        errand_id= getIntent().getStringExtra("errand_id");




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }
        try{
            getDetails();
        }catch (Exception e){
            e.printStackTrace();
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.getStringExtra("errand_id") != null) {
//                        errand_id= intentgetIntent().getStringExtra("errand_id");
                        errand_id= intent.getStringExtra("errand_id");
                        getDetails();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("errand")
        );


    }

    @Override
    protected void onPause() {
        super.onPause();
       try{unregisterReceiver(receiver);}catch (Exception e){e.printStackTrace();}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{ unregisterReceiver(receiver);}catch(IllegalArgumentException e){
            e.printStackTrace();
        }

    }

    private void getDetails(){
        GetUserErrandByErrandIdRequest.getUserErrandByErrandId(this, errand_id, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("msg");
                    final JSONObject errandDetails= jsonArray.getJSONObject(0);
                    String status = errandDetails.getString("status");
                    globalStatus= errandDetails.getString("status");
//                    Toast.makeText(EseekerErrandFullDetails.this, status, Toast.LENGTH_SHORT).show();
                    if(jsonObject.getBoolean("error")){
                        Toast.makeText(EseekerErrandFullDetails.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }else {
                        if (jsonArray.length() == 1) {
                            if(status.equals("Pending")){
                                TextView txtStatus2, txtOptionName2, txtDatePublished2, txtDescription12,txtDescription22;
                                Button btnCancel;
                                View view2= LayoutInflater.from(EseekerErrandFullDetails.this).inflate(R.layout.activity_eseeker_pending_errand, null);
                                RelativeLayout relativeLayout2= findViewById(R.id.eseeker_fullDetails);
                                relativeLayout2.removeAllViews();
                                txtOptionName2= view2.findViewById(R.id.txtOptionName);
                                txtDatePublished2= view2.findViewById(R.id.txtDatePublished);
                                txtStatus2= view2.findViewById(R.id.txtStatus);
                                txtDescription12= view2.findViewById(R.id.txtDescription1);
                                txtDescription22= view2.findViewById(R.id.txtDescription2);
                                btnCancel= view2.findViewById(R.id.btnToggle);
                                   relativeLayout2.addView(view2);

                                try {
                                    updateTimeAgo1(errandDetails.getString("date_published"), txtDatePublished2, "Published");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                txtStatus2.setText(status);
                                txtOptionName2.setText(errandDetails.getString("option_name"));
                                String description = jsonObject.getString("description");
                                String description2 = jsonObject.getString("additional_description");
                                 txtDescription12.setText(description);
                                 txtDescription22.setText(description2);
                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelErrandRequest.cancelErrand(EseekerErrandFullDetails.this, errand_id, new CallBacks() {
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
                        }else{
                            final TextView txtStatus, txtAgo, txtPayment, txtName, txtAddress, txtOptionName, txtDatePublished, txtDescription1,txtDescription2;

                            RatingBar ratingBar;
                            CircleImageView circleImageView;
                            Button btnToggle;
                            View view= LayoutInflater.from(EseekerErrandFullDetails.this).inflate(R.layout.eseeker_full_details_accepted, null);
                            RelativeLayout relativeLayout= findViewById(R.id.eseeker_fullDetails);
                            relativeLayout.removeAllViews();



                            txtStatus= view.findViewById(R.id.txtStatus);
                            txtAgo= view.findViewById(R.id.txtAgo);
                            txtPayment= view.findViewById(R.id.txtPayment);
                            circleImageView= view.findViewById(R.id.imgProfpic);
                            txtName= view.findViewById(R.id.txtName);
                            txtAddress= view.findViewById(R.id.txtAddress);
                            ratingBar= view.findViewById(R.id.ratingBar);
                            txtOptionName= view.findViewById(R.id.txtOptionName);
                                 txtDatePublished= view.findViewById(R.id.txtDatePublished);
                                     txtDescription1= view.findViewById(R.id.txtDescription1);
                            txtDescription2=     view.findViewById(R.id.txtDescription2);
                            btnToggle= view.findViewById(R.id.btnToggle);

                            speedDialView = view.findViewById(R.id.speedDial);
                            speedDialView.hide();

                            speedDialView.addActionItem(
                                    new SpeedDialActionItem.Builder(R.id.fab_message, R.drawable.fab_message).setLabel("Message")
                                            .create()
                            );
                            speedDialView.addActionItem(
                                    new SpeedDialActionItem.Builder(R.id.fab_map, R.drawable.fab_map).setLabel("Map")
                                            .create()
                            );
                            relativeLayout.addView(view);
                            final JSONObject erunnerDetails= jsonArray.getJSONObject(1);
                            final String erunner_username=  erunnerDetails.getString("username");
                            if(status.equals("Waiting for accept")||status.equals("Accepted")){
                                speedDialView.show();
                                btnToggle.setVisibility(View.VISIBLE);
                                btnToggle.setText("Cancel");
                                btnToggle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EseekerErrandFullDetails.this);
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setCancelable(false);
                                        alertDialog.setMessage("Please make sure you provided all details correctly.");
                                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                CancelErrandRequest.cancelErrand(EseekerErrandFullDetails.this, errand_id, new CallBacks() {
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
                                        }).setCancelable(false).create();
                                        try{
                                            alertDialog.show();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }else if (status.equals("On-going")) {
                                speedDialView.show();
                                String ratePerHour= errandDetails.getString("rate_per_hour");
                                String bookingFee= errandDetails.getString("booking_fee");
                                String dateStarted= errandDetails.getString("date_start");
                                updateTotalPayment(dateStarted,ratePerHour,bookingFee, txtPayment);
                                try {
                                    getTimeAgo(dateStarted, txtAgo);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }if(status.equals("Completed")){
                                speedDialView.show();
                                String dateEnd= errandDetails.getString("date_end");
                                txtPayment.setText(errandDetails.getString("total_fee"));
                                try {
                                    getTimeAgo(dateEnd, txtAgo);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Intent intent= new Intent(EseekerErrandFullDetails.this, PayPalService.class);
                                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                                startService(intent);
                                btnToggle.setVisibility(View.VISIBLE);
                                btnToggle.setText("Confirm");
                                btnToggle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        processPayment(txtPayment.getText().toString());

                                    }
                                });
                            }else if(status.equals("Confirmed")){
//                                speedDialView.hide();
                                String dateEnd= errandDetails.getString("date_end");
                                txtPayment.setText(errandDetails.getString("total_fee"));
                                try {
                                    getTimeAgo(dateEnd, txtAgo);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
                                @Override
                                public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                                    switch (speedDialActionItem.getId()) {
                                        case R.id.fab_message:
                                            Intent chatIntent= new Intent(EseekerErrandFullDetails.this, ChatApp.class);
                                            chatIntent.putExtra("errand_id", errand_id);
                                            startActivity(chatIntent);
                                            return false; // true to keep the Speed Dial open
                                        case R.id.fab_map:
                                            startActivity(new Intent(EseekerErrandFullDetails.this, ErunnerMapsActivity.class)
                                                    .putExtra("lat","none")
                                                    .putExtra("type","eseeker")
                                                    .putExtra("username",  erunner_username ));
                                            return false;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            String name = erunnerDetails.getString("firstname") + " " + erunnerDetails.getString("middlename") + " " + erunnerDetails.getString("lastname");
                            String address = erunnerDetails.getString("street") + ", " + erunnerDetails.getString("barangay") + ", " + erunnerDetails.getString("city");
                            Glide.with(getApplicationContext()).load(Constants.URL_GET_IMAGE + erunnerDetails.getString("username") + ".jpg").into(circleImageView);
                            txtStatus.setText(jsonArray.getJSONObject(0).getString("status"));
                            txtName.setText(name);
                            try{
                                ratingBar.setRating(Float.parseFloat(erunnerDetails.getString("rating")));
                            }catch (Exception e){e.printStackTrace();}

                            txtAddress.setText(address);
                            try {
                                updateTimeAgo1(errandDetails.getString("date_published"), txtDatePublished, "Published");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //    txtDatePublished.setText(convertDateToString(errandDetails.getString("date_published")));
                            txtOptionName.setText(errandDetails.getString("option_name"));
                            String description = jsonObject.getString("description");
                            String description2 = jsonObject.getString("additional_description");

                            txtDescription1.setText(description);
                            txtDescription2.setText(description2);
                        }

                    }

                }else{
                    ShowAlert.showAlert(EseekerErrandFullDetails.this, "Something went wrong with your connection.");
//                    Toast.makeText(EseekerErrandFullDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void getTimeAgo(final String givenDate, final TextView txtAgo) throws ParseException {
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
                        txtAgo.setText(timeAgo);

                    };
                });
            }
        },              0, DateUtils.MINUTE_IN_MILLIS);//1000 is a Refreshing Time (1second)
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



    private  void updateTotalPayment(final String dateStarted, final String ratePerHour, final String bookingFee, final TextView txtPayment){
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
                        long total_payment= minutes_15*Integer.parseInt(ratePerHour)+Integer.parseInt(bookingFee);
                        txtPayment.setText(total_payment+"php");
                    }
                });
            }
        },              0, DateUtils.MINUTE_IN_MILLIS*15);//1000 is a Refreshing Time (1second)
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


    private void processPayment(String amount) {
        PayPalPayment payPalPayment= new PayPalPayment(new BigDecimal(String.valueOf(amount)),"PHP","Payment for SugoPh",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent= new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode== PAYPAL_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                    PaymentConfirmation confirmation= data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if(confirmation!=null){
                        try {
                               JSONObject jsonObject1 = new JSONObject(confirmation.toJSONObject().getString("response"));
                               if(jsonObject1.getString("state").equals("approved")){
                                   ConfirmErrandRequest.confirmErrand(EseekerErrandFullDetails.this, errand_id, new CallBacks() {
                                       @Override
                                       public void onSuccess(String response) throws JSONException {
                                           if(response!=null){
                                               JSONObject jsonObject= new JSONObject(response);
                                               if(jsonObject.getBoolean("error")){
                                                   try{
                                                       ShowAlert.showAlert(EseekerErrandFullDetails.this,jsonObject.getString("msg"));
                                                   }catch (Exception e){
                                                       e.printStackTrace();
                                                   }

//                                                   Toast.makeText(EseekerErrandFullDetails.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                               }else{
                                                  // startActivity(new Intent(EseekerErrandFullDetails.this, MainActivity.class));
                                                   final RatingBar ratingBar;
                                                   final CheckBox checkBox;
                                                   final EditText editText;
                                                   Button btnOk, btnSkip;
                                                   LayoutInflater layoutInflater= LayoutInflater.from(EseekerErrandFullDetails.this);
                                                   View view= layoutInflater.inflate(R.layout.evaluation_adapter, null);
                                                   final Dialog dialog= new Dialog(EseekerErrandFullDetails.this);
                                                   dialog.setContentView(view);
                                                   dialog.setCancelable(false);
                                                   ratingBar= view.findViewById(R.id.ratingBar);
                                                   checkBox= view.findViewById(R.id.checkboxReport);
                                                   editText= view.findViewById(R.id.txtFeedback);
                                                   final Validation validation= new Validation();





                                                   btnOk= view.findViewById(R.id.btnOk);
                                                   btnSkip= view.findViewById(R.id.btnSkip);

                                                   btnSkip.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           startActivity(new Intent(EseekerErrandFullDetails.this, MainActivity.class));
                                                       }
                                                   });
                                                   btnOk.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           final Boolean feedbackTest= validation.validateNormalInput(editText);
                                                           final float rate= ratingBar.getRating();
                                                           final Boolean isReported= checkBox.isChecked();
                                                           final String feedback= editText.getText().toString();
                                                           final String report= String.valueOf(isReported);
                                                           final String rating= String.valueOf(rate);
                                                           if(feedbackTest){
                                                               EvaluationRequest.evaluateErunner(EseekerErrandFullDetails.this, errand_id,
                                                                       rating, feedback, report, new CallBacks() {
                                                                           @Override
                                                                           public void onSuccess(String response) throws JSONException {
                                                                               if(response!=null){
                                                                                   JSONObject jsonObject25 =new JSONObject(response);
                                                                                   if(jsonObject25.getBoolean("error")){
                                                                                       Toast.makeText(EseekerErrandFullDetails.this,jsonObject25.getString("msg") ,Toast.LENGTH_SHORT).show();
                                                                                   }else{
                                                                                       startActivity(new Intent(EseekerErrandFullDetails.this, MainActivity.class));
                                                                                   }
                                                                               }else{
                                                                                   ShowAlert.showAlert(EseekerErrandFullDetails.this,"Something went wrong with your connection.");

//                                                                                   Toast.makeText(EseekerErrandFullDetails.this, "Response is null", Toast.LENGTH_SHORT).show();
                                                                               }
                                                                           }
                                                                       });
                                                           }


                                                       }
                                                   });

                                                   try{
                                                       dialog.show();
                                                   }catch (Exception e){
                                                       e.printStackTrace();
                                                   }



                                               }
                                           }else{
                                               ShowAlert.showAlert(EseekerErrandFullDetails.this,"Something went wrong with your conneccton.");

//                                               Toast.makeText(EseekerErrandFullDetails.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                               }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        ShowAlert.showAlert(EseekerErrandFullDetails.this,"Something went wrong with your conneccton.");

//                        Toast.makeText(this, "Something went wrong, confirmation is null!", Toast.LENGTH_SHORT).show();
                    }
            }else if(resultCode==Activity.RESULT_CANCELED){
            }
        }else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }

    }


}

//                        if (jsonArray.length() == 1) {
//                            Toast.makeText(EseekerErrandFullDetails.this, status, Toast.LENGTH_SHORT).show();
//                                     if(status.equals("Pending")){
//                                linearLayoutProfile.getLayoutParams().height=1;
//                                linearLayoutProfile.requestLayout();
//                                btnToggle.setVisibility(View.VISIBLE);
//                                btnToggle.setBackgroundResource(R.drawable.buttonstyle_negative);
//                                btnToggle.setText("Cancel Errand");
//
//                                btnToggle.setEnabled(true);
//                                btnToggle.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        CancelErrandRequest.cancelErrand(EseekerErrandFullDetails.this, errand_id, new CallBacks() {
//                                            @Override
//                                            public void onSuccess(String response) throws JSONException {
//                                                if(response!=null){
//                                                    JSONObject jsonObject = new JSONObject(response);
//                                                    if(jsonObject.getBoolean("error")){
//                                                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
//                                                    }else{
//                                                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
//                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                                    }
//                                                }else{
//                                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                });
//                            }
//                        }else{

