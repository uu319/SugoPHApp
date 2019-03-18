package com.example.pikot.sugophapp.Erunner;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pikot.sugophapp.Eseeker.EseekerPostDocumentActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetErunnerLocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ErunnerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat;
    double longi;
    Button btnBack;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erunner_maps);
        btnBack= findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lat=getIntent().getDoubleExtra("lat", 0);
        longi= getIntent().getDoubleExtra("long", 0);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(getIntent().getStringExtra("type").equals("erunner")){
            LatLng sydney = new LatLng(lat, longi);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Your eseeker's location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));

        }else{
           updateErunnerLocation(getIntent().getStringExtra("username"), mMap);

        }
    }

    private void updateErunnerLocation(final String username, final GoogleMap googleMap){
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            gerErunnerLocation(username, googleMap);
                        }catch(NullPointerException e){

                        }
                    }
                });
            }
        },              0,DateUtils.SECOND_IN_MILLIS*15);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            timer.cancel();
        }catch (NullPointerException e){

        }

    }

    private void gerErunnerLocation(String username, final GoogleMap gMap){
        GetErunnerLocationRequest.getErunnerLocation(this, username, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    String location= jsonObject.getString("msg");
                    String[] latlong= location.split(",");
                    LatLng erunner_location= new LatLng(Double.valueOf(latlong[0]), Double.valueOf(latlong[1]));
                    gMap.clear();
                    gMap.addMarker(new MarkerOptions().position(erunner_location).title("Your erunner's location"));
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(erunner_location));
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(erunner_location,17));
                }else{
                    ShowAlert.showAlert(ErunnerMapsActivity.this, "Problem fetching your location.");
                }
            }
        });
    }
}
