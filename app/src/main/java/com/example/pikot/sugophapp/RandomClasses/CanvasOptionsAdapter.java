package com.example.pikot.sugophapp.RandomClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pikot.sugophapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CanvasOptionsAdapter extends BaseAdapter {
    ArrayList<CanvasOptions> canvasOptions;
    Context context;

    public CanvasOptionsAdapter(Context context, ArrayList<CanvasOptions> canvasOptions) {
        this.canvasOptions = canvasOptions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return canvasOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return canvasOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.canvas_option_listview_adapter,parent,false);
            viewHolder= new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final CanvasOptions canvasOptions= (CanvasOptions) getItem(position);
        viewHolder.txtName.setText(canvasOptions.getOptionName());
        return convertView;
    }

    class ViewHolder{
        TextView txtName;

        public ViewHolder(View view) {
            this.txtName = view.findViewById(R.id.txtOption);
        }
    }
}
