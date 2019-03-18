package com.example.pikot.sugophapp.Erunner;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.Eseeker.EseekerPostDocumentActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Errands;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetUserErrandsByUsernameRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErunnerHistoryFragment extends Fragment {
    private BroadcastReceiver receiver;
    SwipeRefreshLayout pullToRefresh;
//    SwipeRefreshLayout pullToRefresh2;

    Errands errands;
    ArrayList<Errands> errandArrayList;
    ArrayList<Errands> errandsArrayListSort;
    FloatingActionButton fabWallet;
    View view;
    View view2;
    FrameLayout frameLayout;
    int textlength= 0;
    ErunnerMyErrandAdapter adapter;
    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            newErrandNotif();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_erunner_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            startActivity(new Intent(getActivity(), Login.class));
        }
        frameLayout= getActivity().findViewById(R.id.listview_layout);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            getErrands();
        }catch (Exception e){
            e.printStackTrace();
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
//                    Toast.makeText(context, intent.getStringExtra("channel")+"", Toast.LENGTH_SHORT).show();
                    getErrands();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getActivity().registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver), new IntentFilter("errand"));
    }

    private void getErrands(){

        GetUserErrandsByUsernameRequest.getUserErrandsByUsername(getActivity(), new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error")){
                        try{
                            frameLayout.removeView(view);
                            frameLayout.removeAllViews();
                            view= LayoutInflater.from(getActivity()).inflate(R.layout.erunner_no_errand, null);
                            pullToRefresh= view.findViewById(R.id.pullToRefreshErunnerNoErrand);
                            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    try {
                                        getErrands();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    pullToRefresh.setRefreshing(false);
                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        frameLayout.addView(view);
                    }else{
                        try {
                            frameLayout.removeView(view);
                            frameLayout.removeAllViews();
                            view = LayoutInflater.from(getActivity()).inflate(R.layout.erunner_my_errand_listview, null);
                            pullToRefresh = view.findViewById(R.id.pullToRefreshErunnerListview);
                            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    try {
                                        getErrands();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    pullToRefresh.setRefreshing(false);
                                }
                            });
                            fabWallet = view.findViewById(R.id.fabWallet);

                            fabWallet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getActivity(), WalletActivity.class));
                                }
                            });
                            frameLayout.addView(view);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        errandArrayList= new ArrayList<>();
                        errandsArrayListSort= new ArrayList<>();
                        JSONArray jsonArray=jsonObject.getJSONArray("msg");
                        for(int i=0;i<jsonArray.length();i++) {
                            String optionName = jsonArray.getJSONObject(i).getString("option_name");
                            String errandId = jsonArray.getJSONObject(i).getString("errand_id");
                            String datePublished = jsonArray.getJSONObject(i).getString("date_published_string");
                            String status = jsonArray.getJSONObject(i).getString("status");
                            String username= jsonArray.getJSONObject(i).getString("eseeker_username");
                            String fullname= jsonArray.getJSONObject(i).getString("eseeker_fullname");
                            String errand_category_id= jsonArray.getJSONObject(i).getString("errand_category_id");
                            String viewed= jsonArray.getJSONObject(i).getString("erunner_viewed");

                            errands = new Errands(errandId, optionName, datePublished, status,username,fullname, errand_category_id,viewed);

                                                    errandArrayList.add(errands);
                                                    errandsArrayListSort.add(errands);
                        }

                        try{
                            final ListView errandListview= view.findViewById(R.id.errandListview);
                            adapter = new ErunnerMyErrandAdapter(getActivity(), errandArrayList);
                            final EditText editSearch= view.findViewById(R.id.editSearch);
                            errandListview.setAdapter(adapter);
                            editSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    textlength = editSearch.getText().length();
                                    errandsArrayListSort.clear();
                                    for (int i = 0; i < errandArrayList.size(); i++) {
                                        if (textlength <= errandArrayList.get(i).getOptionName().length()) {
//                                            Log.d("ertyyy",movieNamesArrayList.get(i).getMovieName().toLowerCase().trim());
                                            if (errandArrayList.get(i).getOptionName().toLowerCase().trim().contains(
                                                    editSearch.getText().toString().toLowerCase().trim())
                                                    || errandArrayList.get(i).getDatePublished().toLowerCase().trim().contains(
                                                    editSearch.getText().toString().toLowerCase().trim())
                                                    || errandArrayList.get(i).getStatus().toLowerCase().trim().contains(
                                                    editSearch.getText().toString().toLowerCase().trim())
                                                    || errandArrayList.get(i).getFullname().toLowerCase().trim().contains(
                                                    editSearch.getText().toString().toLowerCase().trim())) {
                                                errandsArrayListSort.add(errandArrayList.get(i));
                                            }
                                        }
                                    }
                                    adapter = new ErunnerMyErrandAdapter(getActivity(), errandsArrayListSort);
                                    errandListview.setAdapter(adapter);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                }else{
                    ShowAlert.showAlert(getActivity(), "Something went wrong with your connection.");
//                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void newErrandNotif() {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "LoginChannel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent2= new Intent(getActivity(), MainActivity.class);
        int request_code= 2;
        PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(), request_code, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notif_icon)
                .setTicker("Hearty365")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("New Errand")
                .setContentText("You have a new errand, check it out!.")
                .setContentInfo("Info").addAction(R.drawable.notif_icon, "Go to your errands", pendingIntent).setAutoCancel(true);
        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }
}
