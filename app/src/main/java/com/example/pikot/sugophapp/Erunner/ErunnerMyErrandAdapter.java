package com.example.pikot.sugophapp.Erunner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ErunnerMyErrandAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Errands> errand; //data source of the list adapter

    //public constructor
    public ErunnerMyErrandAdapter(Context context, ArrayList<Errands> errand) {
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

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

//        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.eseeker_errand_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        final Errands currentItem = (Errands) getItem(position);
        viewHolder.optionName.setText(currentItem.getOptionName());
        viewHolder.datePublished.setText("Date Posted:  "+currentItem.getDatePublished());
        viewHolder.errandStatus.setText("Status:  "+currentItem.getStatus());
        viewHolder.fullname.setText("Eseeker name:  "+currentItem.getFullname());
        Glide.with(context).load(Constants.URL_GET_IMAGE + currentItem.getErrand_category_id() + ".png").into(viewHolder.imgUser);

//        if(currentItem.getViewed().equals("true")){
////            viewHolder.imgErrandFullDetails.setImageDrawable(context.getDrawable(R.drawable.ic_keyboard_arrow_right_red_24dp));
////            viewHolder.optionName.setTextColor(context.getColor(R.color.colorRedArrow));
////            viewHolder.datePublished.setTextColor(context.getColor(R.color.colorRedArrow));
////            viewHolder.errandStatus.setTextColor(context.getColor(R.color.colorRedArrow));
//            viewHolder.viewNotif.setVisibility(View.INVISIBLE);
//        }
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
//                Toast.makeText(context, currentItem.getErrandId(), Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(context, ErunnerMyErrandActivity.class);
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
        TextView username;
        ImageView imgErrandFullDetails;
        ImageView imgUser;
        ImageButton viewNotif;

        public ViewHolder(View view) {
            optionName = view.findViewById(R.id.txtOptionName);
            datePublished = view.findViewById(R.id.txtPublished);
            errandStatus = view.findViewById(R.id.txtStatus);
            imgErrandFullDetails= view.findViewById(R.id.imgErrandFulDetails);
            fullname= view.findViewById(R.id.txtName);
            imgUser= view.findViewById(R.id.imgUser);
            viewNotif= view.findViewById(R.id.viewNotif);
        }
    }
}