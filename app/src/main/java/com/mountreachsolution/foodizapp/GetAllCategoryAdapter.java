package com.mountreachsolution.foodizapp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class GetAllCategoryAdapter extends BaseAdapter {
    List<POJOGetAllCategories> pojoGetAllCategories;
    Activity activity;

    public GetAllCategoryAdapter(List<POJOGetAllCategories> list, Activity activity) {
        this.pojoGetAllCategories = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pojoGetAllCategories.size();
    }

    @Override
    public Object getItem(int position) {

        return pojoGetAllCategories.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view==null){
            holder = new ViewHolder();
           view= inflater.inflate(R.layout.lv_category_details_design,null);
            holder.ivCategoryImage=view.findViewById(R.id.ivCategoryImage);
            holder.tvCategoryName=view.findViewById(R.id.tvCategoryName);
            holder.cvCategoryDetails=view.findViewById(R.id.cvcategorydetailsdesign);
            view.setTag(holder);
        }else {
            holder=(ViewHolder) view.getTag();
        }
        final POJOGetAllCategories obj = pojoGetAllCategories.get(position);
        holder.tvCategoryName.setText(obj.getCategoryName());
        Glide.with(activity)
                .load("http://192.168.113.99:80/FoodizAPI/images/"+obj.getCategoryImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable disk caching
                .skipMemoryCache(true)  // Skip memory caching
                .error(R.drawable.icon_menu_category)
                .placeholder(R.drawable.icon_menu_home)
                .into(holder.ivCategoryImage);
        holder.cvCategoryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity,CategoryWiseDishActivity.class);
                i.putExtra("categoryname",obj.getCategoryName());
                activity.startActivity(i);
            }
        });

        return view;
    }

    class ViewHolder {
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        CardView cvCategoryDetails;
    }
}
