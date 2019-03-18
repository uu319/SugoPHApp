package com.example.pikot.sugophapp.Eseeker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Erunner.ErunnerMyErrandAdapter;
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
public class EseekerMyErrandFragment extends Fragment {

    ArrayList<Errands> errandArrayList= new ArrayList<Errands>();
    ArrayList<Errands> errandArrayListSort= new ArrayList<Errands>();
    Errands errands;
    EseekerMyErrandAdapter adapter;
    int textlength= 0;
    private BroadcastReceiver receiver;
    SwipeRefreshLayout pullToRefresh;
    FrameLayout frameLayout;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_eseeker_my_errand, container, false);
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
                    getErrands();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getActivity().registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver), new IntentFilter("errand"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            startActivity(new Intent(getActivity(), Login.class));
        }
        frameLayout= getActivity().findViewById(R.id.errands_layout);




    }


    private void getErrands() {

        try{
        GetUserErrandsByUsernameRequest.getUserErrandsByUsername(getActivity(), new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        try {
                            frameLayout.removeAllViews();
                            frameLayout.removeView(view);
                            view= LayoutInflater.from(getActivity()).inflate(R.layout.eseeker_no_errands_view, null);
                            pullToRefresh= view.findViewById(R.id.pullToRefreshEseekerNoErrand);
                            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    try{
                                        getErrands();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    pullToRefresh.setRefreshing(false);
                                }
                            });
                            frameLayout.addView(view);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        try{
                            frameLayout.removeAllViews();
                            frameLayout.removeView(view);
                            view= LayoutInflater.from(getActivity()).inflate(R.layout.eseeker_my_errands_listview, null);
                            pullToRefresh= view.findViewById(R.id.pullToRefreshEseekerMyErrand);
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
                            frameLayout.addView(view);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        JSONArray jsonArray = jsonObject.getJSONArray("msg");
                        errandArrayList = new ArrayList<Errands>();
                        errandArrayListSort= new ArrayList<Errands>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String optionName = jsonArray.getJSONObject(i).getString("option_name");
                            String errandId = jsonArray.getJSONObject(i).getString("errand_id");
                            String datePublished = jsonArray.getJSONObject(i).getString("date_published_string");
                            String status = jsonArray.getJSONObject(i).getString("status");
                            String username = jsonArray.getJSONObject(i).getString("erunner_username");
                            String errand_category_id = jsonArray.getJSONObject(i).getString("errand_category_id");
                            String viewed = jsonArray.getJSONObject(i).getString("eseeker_viewed");
                            String fullname;
                            if (status.equals("Pending")) {
                                fullname = "No Errunner yet.";
                            } else {
                                fullname = jsonArray.getJSONObject(i).getString("erunner_fullname");
                            }

                            errands = new Errands(errandId, optionName, datePublished, status, username, fullname, errand_category_id, viewed);
                            errandArrayList.add(errands);
                            errandArrayListSort.add(errands);
                        }
                        try {
                            final ListView errandListview = view.findViewById(R.id.errandListview);
                            final EditText editSearch= view.findViewById(R.id.editSearch2);
                            adapter = new EseekerMyErrandAdapter(getActivity(), errandArrayList);
                            errandListview.setAdapter(adapter);
                            editSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    textlength = editSearch.getText().length();
                                    errandArrayListSort.clear();
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
                                                errandArrayListSort.add(errandArrayList.get(i));
                                            }
                                        }
                                    }
                                    adapter = new EseekerMyErrandAdapter(getActivity(), errandArrayListSort);
                                    errandListview.setAdapter(adapter);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    ShowAlert.showAlert(getActivity(), "Something went wrong with your connection.");
                }
            }
        });
    }catch (Exception e){e.printStackTrace();}

    }
}
