package com.example.pikot.sugophapp.Eseeker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.Eseeker.EseekerErrandFullDetails;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.Errands;
import com.example.pikot.sugophapp.Requests.ViewUpdateRequest;

import org.json.JSONException;

import java.util.ArrayList;

public class EseekerMyErrandAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Errands> errand; //data source of the list adapter

    //public constructor
    public EseekerMyErrandAdapter(Context context, ArrayList<Errands> errand) {
        this.context = context;
        this.errand = errand;
    }

    @Override
    public int getCount() {
        return errand.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return errand.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.eseeker_errand_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Errands currentItem = (Errands) getItem(position);
        viewHolder.optionName.setText(currentItem.getOptionName());
        viewHolder.datePublished.setText("Date Posted:  "+currentItem.getDatePublished());
        viewHolder.errandStatus.setText("Status:  "+currentItem.getStatus());
        viewHolder.fullname.setText(currentItem.getFullname());
        Glide.with(context).load(Constants.URL_GET_IMAGE + currentItem.getErrand_category_id() + ".png").into(viewHolder.imgUser);

        if(currentItem.getViewed().equals("false")){
            viewHolder.viewNotif.setVisibility(View.VISIBLE);
        }else{

        }
        viewHolder.imgErrandFullDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUpdateRequest.updateView(context, currentItem.getErrandId(), new CallBacks() {
                    @Override
                    public void onSuccess(String response) throws JSONException {

                    }
                });
                Intent intent= new Intent(context, EseekerErrandFullDetails.class);
                intent.putExtra("errand_id", currentItem.getErrandId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView optionName;
        TextView datePublished;
        TextView errandStatus;
        TextView fullname;
        ImageView imgUser;
        ImageView imgErrandFullDetails;
        ImageButton viewNotif;

        public ViewHolder(View view) {
            optionName = view.findViewById(R.id.txtOptionName);
            datePublished = view.findViewById(R.id.txtPublished);
            errandStatus = view.findViewById(R.id.txtStatus);
            imgErrandFullDetails= view.findViewById(R.id.imgErrandFulDetails);
            imgUser= view.findViewById(R.id.imgUser);
            fullname=view.findViewById(R.id.txtName);
            viewNotif= view.findViewById(R.id.viewNotif);
        }
    }
}