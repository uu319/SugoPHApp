package com.example.pikot.sugophapp.Erunner;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.Both.RegName;
import com.example.pikot.sugophapp.Both.TermsAndConditions;
import com.example.pikot.sugophapp.Eseeker.EseekerPostDocumentActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Errands;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetAllOptionsByUsername;
import com.example.pikot.sugophapp.Requests.UpdateErunnerLocationRequest;
import com.example.pikot.sugophapp.Requests.UpdateStatusRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErunnerSettings extends Fragment {
    ListView listView;
    ArrayList<ErrandOptions> errandOptionList;
    ErrandOptionsAdapter errandOptionsAdapter;
    Switch aSwitch;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String currentLocation = null;

    SwipeRefreshLayout pullToRefresh;

    public ErunnerSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_erunner_settings, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getActivity().findViewById(R.id.listViewOptions);
        aSwitch = getActivity().findViewById(R.id.updateSwitch);
        try{
            pullToRefresh= getActivity().findViewById(R.id.pullToRefreshSettings);

            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    getAllOptionsByUsername();
                    pullToRefresh.setRefreshing(false);

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }









    }

    @Override
    public void onResume() {
        super.onResume();
        getAllOptionsByUsername();
        try{
            aSwitch.setChecked(!SharedPrefManager.getInstance(getActivity()).getKeyStatus().equals("inactive"));
        }catch (Exception e){
            e.printStackTrace();
        }

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{currentLocation = location.getLatitude() + "," + location.getLongitude();
                    if(aSwitch.isChecked()) {
//                        Toast.makeText(getActivity(), currentLocation, Toast.LENGTH_SHORT).show();
                        UpdateErunnerLocationRequest.updateLocation(getActivity(), currentLocation, new CallBacks() {
                            @Override
                            public void onSuccess(String response) throws JSONException {
                            }
                        });
                    }else{
                        locationManager.removeUpdates(locationListener);
                    }

                }catch (Exception e){
                    e.printStackTrace();
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

        try{
            if(!SharedPrefManager.getInstance(getActivity()).getKeyStatus().equals("active")
                    && !SharedPrefManager.getInstance(getActivity()).getKeyStatus().equals("inactive")){
                aSwitch.setEnabled(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }




        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateStatus(SharedPrefManager.getInstance(getActivity()).getKeyStatus());
                if (isChecked) {
                    locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DateUtils.SECOND_IN_MILLIS * 15, 0, locationListener);
                }else{
                    locationManager.removeUpdates(locationListener);
                }
            }
        });

            if(aSwitch.isChecked()) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DateUtils.SECOND_IN_MILLIS * 15, 0, locationListener);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            locationManager.removeUpdates(locationListener);
        }


    }
    @Override
    public void onPause() {
        super.onPause();

    }

    public void getAllOptionsByUsername(){
        try{errandOptionList.clear();}catch (Exception e){e.printStackTrace();}
        GetAllOptionsByUsername.getAllOptionsByUsername(getActivity(), new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("msg");
                    errandOptionList= new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++) {
                        String optionName = jsonArray.getJSONObject(i).getString("option_name");
                        String status = jsonArray.getJSONObject(i).getString("status");
                        String option_id = jsonArray.getJSONObject(i).getString("option_id");
                        errandOptionList.add(new ErrandOptions(option_id, optionName, status));
                    }
                    try {
                        errandOptionsAdapter= new ErrandOptionsAdapter(getActivity(), errandOptionList);
                        listView.setAdapter(errandOptionsAdapter);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    ShowAlert.showAlert(getActivity(), "Something went wrong with your connection.");
//                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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


    private void updateStatus(final String status){
        UpdateStatusRequest.updateStatus(getActivity(), status, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error")){
                        ShowAlert.showAlert(getActivity(),jsonObject.getString("msg"));
                    }else{
                        if(status.equals("active")){
                            SharedPrefManager.getInstance(getActivity()).setKeyStatus("inactive");
                        }else{
                            SharedPrefManager.getInstance(getActivity()).setKeyStatus("active");
                        }
                    }
                }else{
                    ShowAlert.showAlert(getActivity(),"Something went wrong with your internet connection.");
                }


            }
        });
    }



}
