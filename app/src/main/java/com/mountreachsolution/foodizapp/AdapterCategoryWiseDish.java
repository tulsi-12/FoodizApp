package com.mountreachsolution.foodizapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdapterCategoryWiseDish extends BaseAdapter {

    List<POJOCategoryWiseDish> pojoCategoryWiseDishes;
    Activity activity;

    public AdapterCategoryWiseDish(List<POJOCategoryWiseDish> pojoCategoryWiseDishes, Activity activity) {
        this.pojoCategoryWiseDishes = pojoCategoryWiseDishes;
        this.activity = activity;
    }

    @Override
    public int getCount() {

        return pojoCategoryWiseDishes.size();
    }

    @Override
    public Object getItem(int position) {

        return pojoCategoryWiseDishes.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder ;
        LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view==null)
        {
            holder=new ViewHolder();
            view = inflater.inflate(R.layout.lv_categorywise_dishes,null);
            holder.ivDishImage=view.findViewById(R.id.ivCategoryWiseDishImage);
            holder.tvRestaurantName=view.findViewById(R.id.tvCategoryWiseDishRestaurantName);
            holder.tvRating=view.findViewById(R.id.tvCategoryWiseDishRestaurantRating);
            holder.tvDishName=view.findViewById(R.id.tvCategoryWiseDishDishName);
            holder.tvDishCategory=view.findViewById(R.id.tvCategoryWiseDishDishcategory);
            holder.tvDishPrice=view.findViewById(R.id.tvCategoryWiseDishDishprice);
            holder.tvDishOffer=view.findViewById(R.id.tvCategoryWiseDishDishOffer);
            holder.tvDishDescription=view.findViewById(R.id.tvCategoryWiseDishDishDescription);

            view.setTag(holder);
        }else{
            holder=(ViewHolder) view.getTag();
        }

        POJOCategoryWiseDish obj = pojoCategoryWiseDishes.get(position);
        holder.tvRestaurantName.setText(obj.getRestname());
        holder.tvRating.setText(obj.getDishrating());
        holder.tvDishName.setText(obj.getDishname());
        holder.tvDishCategory.setText(obj.getDishcategory());
        holder.tvDishPrice.setText(obj.getDishprice());
        holder.tvDishOffer.setText(obj.getDishoffer());
        holder.tvDishDescription.setText(obj.getDishdescription());

        Glide.with(activity).load("http://192.168.113.99:80/FoodizAPI/images/"+obj.getDishimg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable disk caching
                .skipMemoryCache(true)  // Skip memory caching
                .error(R.drawable.icon_menu_category)
                .into(holder.ivDishImage);

        return view;
    }
    class ViewHolder{
        ImageView ivDishImage;
        TextView tvRestaurantName,tvRating,tvDishName,tvDishCategory,tvDishPrice,tvDishOffer,tvDishDescription;
    }
}
