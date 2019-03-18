package com.example.pikot.sugophapp.RandomClasses;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.Requests.GetUserErrandsByUsernameRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Snippets {
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    {
        //BroadcastReceiver
//        BroadcastReceiver receiver;
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
//        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),new IntentFilter("Event")
//        );


        //Sender
//        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
//        Intent intent = new Intent("Event");
//        broadcaster.sendBroadcast(intent);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    {
        //Notification

//        private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
//        private long FASTEST_INTERVAL = 2000; /* 2 sec */
//        private void buildNotif() {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "LoginChannel";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
//
//            // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//        Intent intent= new Intent(this, Login.class);
//        int request_code= 2;
//        PendingIntent pendingIntent=PendingIntent.getActivity(this, request_code, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.ic_chevron_right_black_24dp)
//                .setTicker("Hearty365")
//                //     .setPriority(Notification.PRIORITY_MAX)
//                .setContentTitle("Default notification")
//                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
//                .setContentInfo("Info").addAction(R.drawable.ic_chevron_right_black_24dp, "Login", pendingIntent).setAutoCancel(true);
//        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
//    }
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    {

//        LocationCallback locationCallback;

//        locationCallback= new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                onLocationChanged(locationResult.getLastLocation());
//                Toast.makeText(Login.this, locationResult.getLastLocation().getLatitude() + "", Toast.LENGTH_SHORT).show();
//            }
//        };


//        public static boolean isLocationEnabled(Context context) {
//        int locationMode = 0;
//        String locationProviders;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            try {
//                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//                return false;
//            }
//            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
//
//        }else{
//            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            return !TextUtils.isEmpty(locationProviders);
//        }
//
//
//    }

//        @SuppressLint("MissingPermission")
//        public void getLastLocation() {
//        // Get last known recent location using new Google Play Services SDK (v11+)
//        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
//        locationClient.getLastLocation()
//                .addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            onLocationChanged(location);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
//                        e.printStackTrace();
//                    }
//                });
//    }

//        public void onLocationChanged(Location location) {
//        currentLocation=location.getLatitude()+"-"+location.getLongitude();
//    }


//        @SuppressLint("MissingPermission")
//        protected void startLocationUpdates() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(mLocationRequest);
//        LocationSettingsRequest locationSettingsRequest = builder.build();
//        builder.setAlwaysShow(true);
//
//        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
//        settingsClient.checkLocationSettings(locationSettingsRequest);
//        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());
//    }

    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    {
//        FetchById

//        private static ProgressDialog progressDialog;
//        public static void getErrandCategory(final Context context, final CallBacks callBacks) {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_ERRANDS,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            callBacks.onSuccess(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        try {
//                            callBacks.onSuccess(null);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
//    }

    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    {
        //Callback
//        private void getErrands(){
//        GetUserErrandsByUsernameRequest.getUserErrandsByUsername(getActivity(), new CallBacks() {
//            @Override
//            public void onSuccess(String response) throws JSONException {
//                if(response!=null){
//                    JSONObject jsonObject= new JSONObject(response);
//                    if(jsonObject.getBoolean("error")){
//                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                    }else{
//                        JSONArray jsonArray=jsonObject.getJSONArray("msg");
//
//                        for(int i=0;i<jsonArray.length();i++) {
//                            String optionName = jsonArray.getJSONObject(i).getString("option_name");
//                            String errandId = jsonArray.getJSONObject(i).getString("errand_id");
//                            String datePublished = jsonArray.getJSONObject(i).getString("date_published");
//                            String status = jsonArray.getJSONObject(i).getString("status");
//                            errands = new Errands(errandId, optionName, datePublished, status);
//                            errandArrayList.add(errands);
//                        }
//                        final ListView errandListview= getActivity().findViewById(R.id.errandListview);
//                        final EseekerMyErrandAdapter adapter = new EseekerMyErrandAdapter(getActivity(), errandArrayList);
//                        errandListview.setAdapter(adapter);
//                    }
//                }else{
//                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
    }

    {

        //Runable

//        Handler handler = new Handler();
//// Define the code block to be executed
//        private Runnable runnableCode = new Runnable() {
//            @Override
//            public void run() {
//
//                sendVolleyRequestToServer(); // Volley Request
//
//                // Repeat this the same runnable code block again another 2 seconds
//                handler.postDelayed(runnableCode, 2000);
//            }
//        };
//// Start the initial runnable task by posting through the handler
//        handler.post(runnableCode);
    }

}
