package com.example.pikot.sugophapp.RandomClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pikot.sugophapp.R;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Chat> chat;

          public ChatAdapter(Context context, ArrayList<Chat> chat) {
        this.context = context;
        this.chat = chat;
    }

    @Override
    public int getCount() {
        return chat.size();
    }

    @Override
    public Object getItem(int position) {
        return chat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat chat= (Chat) getItem(position);
        ViewHolder viewHolder;

            if(chat.isLeft()){
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_left,null);
            }else{
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_right, null);
            }
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);


        viewHolder.txtDate.setText(chat.getDate());
        viewHolder.txtMessage.setText(chat.getMessaage());
        return convertView;
    }

    public class ViewHolder{
        TextView txtMessage;
        TextView txtDate;

        public ViewHolder(View view) {
            this.txtMessage = view.findViewById(R.id.txtMessage);
            this.txtDate = view.findViewById(R.id.txtDate);
        }
    }
}
