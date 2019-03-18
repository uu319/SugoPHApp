package com.example.pikot.sugophapp.Both;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Feedback;
import com.example.pikot.sugophapp.RandomClasses.FeedbackAdapter;
import com.example.pikot.sugophapp.Requests.GetFeedbacksRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFeedback extends Fragment {
    ArrayList<Feedback> feedbacks;
    FeedbackAdapter feedbackAdapter;
    ListView feedBackListview;
    SwipeRefreshLayout pullToRefresh;
    public FragmentFeedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_feedback, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        feedBackListview= getActivity().findViewById(R.id.feedBackListview);
        feedbacks= new ArrayList<>();
        pullToRefresh= getActivity().findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeedbacks();
                pullToRefresh.setRefreshing(false);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            getFeedbacks();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void getFeedbacks(){
        try{feedbacks.clear();}catch(Exception e){e.printStackTrace();}
        GetFeedbacksRequest.getFeedbacks(getActivity(), new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("msg");

                    for(int i=0;i<jsonArray.length();i++) {
                        feedbacks.add(new Feedback(jsonArray.getJSONObject(i).getString("errand_id"),
                                jsonArray.getJSONObject(i).getString("option_name"),jsonArray.getJSONObject(i).getString("rating"),
                                jsonArray.getJSONObject(i).getString("feedback"), jsonArray.getJSONObject(i).getString("date")));
                    }
                    feedbackAdapter= new FeedbackAdapter(getActivity(), feedbacks);
                    feedBackListview.setAdapter(feedbackAdapter);
                }
            }
        });
    }
}
