package com.example.pikot.sugophapp.Both;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.Erunner.ErunnerHomeFragment;
import com.example.pikot.sugophapp.Erunner.ErunnerMyErrandActivity;
import com.example.pikot.sugophapp.Eseeker.EseekerHomeFragment;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetUserStatus;
import com.example.pikot.sugophapp.Requests.LogoutRequest;
import com.example.pikot.sugophapp.Requests.UpdateErunnerLocationRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager = getSupportFragmentManager();
    int state;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String currentLocation = null;

    private String globalStatus;

    Button btnTest;

    TextView txtUsername, txtStatus;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state=0;



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout mParent = ( LinearLayout ) navigationView.getHeaderView( 0 );
        ImageView imgvw = mParent.findViewById(R.id.imageView);
        txtUsername= mParent.findViewById(R.id.txtUsername);
        txtStatus= mParent.findViewById(R.id.txtStatus);

        txtUsername.setText(SharedPrefManager.getInstance(this).getKeyUsername());
        Glide.with(this).load(Constants.URL_GET_IMAGE+SharedPrefManager.getInstance(this.getApplicationContext()).getKeyUsername()+".jpg").into(imgvw);

        //-------------------------------------------------------------------------------


        //-------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            if(SharedPrefManager.getInstance(MainActivity.this).getKeyType().equals("eseeker")) {
                EseekerHomeFragment eseekerHomeFragment = new EseekerHomeFragment();
                fragmentManager.popBackStack();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction().add(R.id.fragment , eseekerHomeFragment);
                fragmentTransaction.addToBackStack("My Screen");
                fragmentTransaction.commit();
            }else{
                ErunnerHomeFragment fragment_home = new ErunnerHomeFragment();
                fragmentManager.popBackStack();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment, fragment_home);
                fragmentTransaction.addToBackStack("My Screen");
                fragmentTransaction.commit();
            }
        }else{
            startActivity(new Intent(this,Login.class));
        }
        }






    @Override
    protected void onResume() {
        super.onResume();
        try{
            getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{

        }catch(NullPointerException e){
            e.printStackTrace();
        }





    }

    private void getStatus(){
        GetUserStatus.getStatus(this, SharedPrefManager.getInstance(this).getKeyUsername(),new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error")){
                        ShowAlert.showAlert(MainActivity.this, jsonObject.getString("msg"));
                    }else{
                        globalStatus= jsonObject.getString("msg");
                        SharedPrefManager.getInstance(MainActivity.this).setKeyStatus(globalStatus);
                        try{
                            if(globalStatus.equals("pending")) {
                                startActivity(new Intent(MainActivity.this, ErunnerPendingActivity.class));
                            }else if(globalStatus.equals("banned")){
                                startActivity(new Intent(MainActivity.this, ErunnerBannedActivity.class));
                            }else if(globalStatus.equals("suspended")){
                                startActivity(new Intent(MainActivity.this, ErunnerSuspendedActivity.class));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        txtStatus.setText(SharedPrefManager.getInstance(MainActivity.this).getKeyStatus());
                    }
                }else{
                    ShowAlert.showAlert(MainActivity.this, "Something went wrong");
                }
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(receiver);
//        receiver=null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            if(state==1){
                startActivity(new Intent(this,MainActivity.class));
            }else{
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class ));
        } else if (id == R.id.nav_profile) {
            state=1;
            ProfileFragment _profileFragment = new ProfileFragment();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction= fragmentManager.beginTransaction().replace(R.id.fragment, _profileFragment);
            fragmentTransaction.addToBackStack("My Screen");
            fragmentTransaction.commit();

//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_feedback) {
            state=1;
            FragmentFeedback _feedbackFragment = new FragmentFeedback();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction= fragmentManager.beginTransaction().replace(R.id.fragment, _feedbackFragment);
            fragmentTransaction.addToBackStack("My Screen");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
//
            LogoutRequest.logoutRequest(this);


//            SharedPrefManager.getInstance(this).logout();

        }else if(id==R.id.nav_password){
            startActivity(new Intent(this, ChangePasswordActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
