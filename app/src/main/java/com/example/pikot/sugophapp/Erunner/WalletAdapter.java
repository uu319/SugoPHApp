package com.example.pikot.sugophapp.Erunner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pikot.sugophapp.R;

import java.util.ArrayList;

public class WalletAdapter extends BaseAdapter {
    Context context;
    ArrayList<Wallet> wallets;

    public WalletAdapter(Context context, ArrayList<Wallet> wallets) {
        this.context = context;
        this.wallets = wallets;
    }

    @Override
    public int getCount() {
        return wallets.size();
    }

    @Override
    public Object getItem(int position) {
        return wallets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.wallet_adapter, null, false);
            viewHolder= new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Wallet currentIten= (Wallet) getItem(position);
                                viewHolder.txtAmount.setText(currentIten.getWalletAmount());
                                viewHolder.txtName.setText(currentIten.getWalletName());
                                viewHolder.txtDate.setText(currentIten.getWalletDate());
        return convertView;
    }
    class ViewHolder{
        TextView txtDate, txtName, txtAmount;

        public ViewHolder(View view) {
            this.txtDate = view.findViewById(R.id.txtDate);
            this.txtName= view.findViewById(R.id.txtWalletName);
            this.txtAmount= view.findViewById(R.id.txtWalletAmount);
        }
    }
}
