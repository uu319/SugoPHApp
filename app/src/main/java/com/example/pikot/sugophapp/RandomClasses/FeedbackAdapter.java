package com.example.pikot.sugophapp.RandomClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pikot.sugophapp.R;

import java.util.ArrayList;

public class FeedbackAdapter extends BaseAdapter {
    Context context;
    ArrayList<Feedback> feedbacks;

    public FeedbackAdapter(Context context, ArrayList<Feedback> feedbacks) {
        this.context = context;
        this.feedbacks = feedbacks;
    }


    @Override
    public int getCount() {
        return feedbacks.size();
    }

    @Override
    public Object getItem(int position) {
        return feedbacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.feedback_adapter, null,false);
            viewHolder= new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Feedback currentItem= (Feedback) getItem(position);
        viewHolder.feedbackRating.setRating(Float.valueOf(currentItem.getRating()));
        viewHolder.feedbackName.setText(currentItem.getOption_name());
        viewHolder.feedbackDesc.setText(currentItem.getFeedback());
        viewHolder.feedbackDate.setText(currentItem.getDate());
        return convertView;
    }

    class ViewHolder{
        TextView feedbackName;
        TextView feedbackDesc;
        RatingBar feedbackRating;
        TextView feedbackDate;
        public ViewHolder(View view){
            this.feedbackDesc= view.findViewById(R.id.feedBackDesc);
            this.feedbackName= view.findViewById(R.id.feedBackName);
            this.feedbackRating= view.findViewById(R.id.feedBackRate);
            this.feedbackDate= view.findViewById(R.id.feedBackDate);
        }
    }
}
