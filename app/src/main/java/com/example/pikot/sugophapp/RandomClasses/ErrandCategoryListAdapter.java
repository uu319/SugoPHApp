package com.example.pikot.sugophapp.RandomClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.Category;

import java.util.ArrayList;

public class ErrandCategoryListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Category> category; //data source of the list adapter

    //public constructor
    public ErrandCategoryListAdapter(Context context, ArrayList<Category> category) {
        this.context = context;
        this.category = category;
    }

    @Override
    public int getCount() {
        return category.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return category.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.layout_list_view_row_categories, parent, false);
        }

        // get current item to be displayed
        Category currentItem = (Category) getItem(position);

        // get the TextView for item name and item description
        TextView textViewCategoryName = convertView.findViewById(R.id.txtCategoryName);
        TextView textViewCategoryDescription = convertView.findViewById(R.id.txtCategoryDescription);
        ImageView imageView= convertView.findViewById(R.id.imgCat);

        //sets the text for item name and item description from the current item object
        textViewCategoryName.setText(currentItem.getCategoryName());
        textViewCategoryDescription.setText(currentItem.getCategoryDescription());
        Glide.with(context).load(Constants.URL_GET_IMAGE+currentItem.getCategoryId().trim()+".png").into(imageView);


        // returns the view for the current row
        return convertView;
    }
}