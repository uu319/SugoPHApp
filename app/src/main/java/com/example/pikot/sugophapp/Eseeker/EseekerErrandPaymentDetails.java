package com.example.pikot.sugophapp.Eseeker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.GetLocation;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.CancelErrandRequest;
import com.example.pikot.sugophapp.Requests.GetMatchRequest;
import com.example.pikot.sugophapp.Requests.GetOptionIdByNameRequest;
import com.example.pikot.sugophapp.Requests.GetPaymentDetailsRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.Requests.PostErrandRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class EseekerErrandPaymentDetails extends AppCompatActivity implements View.OnClickListener {
    private Button btnpostErrand;
    private TextView txtBookingFee, txtRatePerHour;
    private String data, start, end;
    ProgressDialog progressDialog;

    private String currentLocation = null;
    private String option_id;
    private String description = "";
    private LocationManager locationManager;
    private LocationListener locationListener;
    Criteria criteria;
    String best;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        best= locationManager.getBestProvider(criteria, true);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation=location.getLatitude()+","+location.getLongitude();
//                Toast.makeText(EseekerErrandPaymentDetails.this, currentLocation+"", Toast.LENGTH_SHORT).show();
                if(currentLocation!=null){
                    try{
                        progressDialog.dismiss();
                    }catch (NullPointerException e){
                        Toast.makeText(EseekerErrandPaymentDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try{
                        postErrand(option_id, description, currentLocation);
                        locationManager.removeUpdates(locationListener);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        setContentView(R.layout.activity_eseeker_errand_payment_details);
        txtBookingFee = findViewById(R.id.txtBookingFee);
        txtRatePerHour = findViewById(R.id.txtRatePerHour);
        btnpostErrand = findViewById(R.id.btnPostErrand);
        btnpostErrand.setOnClickListener(this);
        Intent intent = getIntent();
        description = intent.getStringExtra("description");
        data = intent.getStringExtra("option_name");
        start=intent.getStringExtra("start");
        end=intent.getStringExtra("end");


        getOptionId(data);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }
        if (isLocationEnabled(this)) {
            getLastLocation();
        } else {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }


    }


    @Override
    public void onClick(View v) {
//        if (isLocationEnabled(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }else {
                progressDialog= new ProgressDialog(this);
                progressDialog.setMessage("We are currently fetching your locatiom, please wait.");
                progressDialog.show();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
//        } else {
//            Intent gpsOptionsIntent = new Intent(
//                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(gpsOptionsIntent);
//        }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void onLocationChanged(Location location) {
        currentLocation = location.getLatitude() + "-" + location.getLongitude();
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            if(permissionGranted) {
                locationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    onLocationChanged(location);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }else{
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                onLocationChanged(location);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    });
        }
    }
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }




    private void postErrand(String option_id,String description, String currentLocation){

        PostErrandRequest.postErrand(this, option_id, description, currentLocation, start, end, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                    if(response!=null){
                        final JSONObject jsonObject= new JSONObject(response);
                        if(jsonObject.getBoolean("error")){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                        }else{

                            final String errand_id= jsonObject.getString("errand_id");
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EseekerErrandPaymentDetails.this);
                            alertDialog.setTitle("Alert");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Please make sure you provided all details correctly.");
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    CancelErrandRequest.cancelErrand(EseekerErrandPaymentDetails.this, errand_id, new CallBacks() {
                                        @Override
                                        public void onSuccess(String response) throws JSONException {
                                            if(response!=null){
                                                JSONObject jsonObject = new JSONObject(response);
                                                if(jsonObject.getBoolean("error")){

                                                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                                }else{
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startActivity(new Intent(EseekerErrandPaymentDetails.this, MainActivity.class));
                                    GetMatchRequest.getMatchRequest(EseekerErrandPaymentDetails.this, errand_id, new CallBacks() {
                                        @Override
                                        public void onSuccess(String response) throws JSONException {
                                            Toast.makeText(getApplicationContext(), "Matching...", Toast.LENGTH_LONG).show();
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
                    }else{
                        ShowAlert.showAlert(EseekerErrandPaymentDetails.this,"Something went wrong.");
//                        Toast.makeText(EseekerErrandPaymentDetails.this, "Response is null", Toast.LENGTH_SHORT).show();
                    }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void getPaymentDetails(String data){
        GetPaymentDetailsRequest.getPaymentDetails(this, data, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error")){
                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }else{
                        JSONArray jsonArray= jsonObject.getJSONArray("msg");
                        txtBookingFee.setText("Php "+jsonArray.getJSONObject(0).getString("booking_fee"));
                        txtRatePerHour.setText("Php "+jsonArray.getJSONObject(0).getString("rate_per_hour"));
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getOptionId(String option_name){
        GetOptionIdByNameRequest.getOptionId(this, option_name, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error")){
                        try{
                            ShowAlert.showAlert(EseekerErrandPaymentDetails.this,"Something went wrong with your conneccton.");
                        }catch (Exception e){
                            e.printStackTrace();
                        }



//                        Toast.makeText(getApplicationContext(), jsonObject.getString("Something went wrong"),Toast.LENGTH_LONG).show();
                    }else{
                        JSONArray jsonArray= jsonObject.getJSONArray("msg");
                        option_id =jsonArray.getJSONObject(0).getString("option_id");
                        getPaymentDetails(option_id);
                    }
                }else{
                    ShowAlert.showAlert(EseekerErrandPaymentDetails.this, "Something went wrong with your connection.");
//                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
     }
    }


