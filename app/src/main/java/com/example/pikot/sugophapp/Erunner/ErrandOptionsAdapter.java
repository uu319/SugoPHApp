package com.example.pikot.sugophapp.Erunner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.Requests.UpdateServiceOffered;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ErrandOptionsAdapter extends BaseAdapter {
    Context context;
    ArrayList<ErrandOptions> errandOptions;

    public ErrandOptionsAdapter(Context context, ArrayList<ErrandOptions> errandOptions){
        this.context= context;
        this.errandOptions= errandOptions;
    }

    @Override
    public int getCount() {
        return errandOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return errandOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.erunner_services_adapter,parent, false);
            viewHolder= new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else viewHolder= (ViewHolder) convertView.getTag();
        final ErrandOptions errandOptions= (ErrandOptions) getItem(position);
        viewHolder.textView.setText(errandOptions.getOption_name());
        if(errandOptions.getStatus().equals("unoffered")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
        }else{
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_remove_black_24dp));
        }
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateServiceOffered.updateServiceOffered(context,errandOptions.getOption_id(), errandOptions.getStatus(), new CallBacks() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if(response!=null) {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                if(errandOptions.getStatus().equals("offered")){
                                    viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
                                    errandOptions.setStatus("unoffered");
                                }else{
                                    viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_remove_black_24dp));
                                    errandOptions.setStatus("offered");
                                }
                            }else{
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;
        public  ViewHolder(View view){
            this.textView= view.findViewById(R.id.option_name);
            this.imageView= view.findViewById(R.id.option_image);
        }
    }
}
