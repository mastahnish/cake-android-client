package com.waracle.androidtest.workflow.dishes;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waracle.androidtest.data.Dish;
import com.waracle.androidtest.databinding.ListItemLayoutBinding;
import com.waracle.androidtest.http.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacek on 2018-03-19.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Dish> mItems;
    private ImageLoader mImageLoader;

    public MyAdapter(Activity ac) {
        this(ac, new ArrayList<Dish>());
    }

    public MyAdapter(Activity ac, List<Dish> items) {
        mItems = items;
        mImageLoader = new ImageLoader(ac);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemLayoutBinding binding = ListItemLayoutBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Dish singleDish = mItems.get(position);
        holder.binding.setDish(singleDish);
        holder.binding.desc.setText(singleDish.getDescription());

        mImageLoader.load(singleDish.getUrl(), holder.binding.image);


    }

    @Override
    public int getItemCount() {
        return  mItems != null ? mItems.size() : 0;
    }



    public void setItems(List<Dish> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ListItemLayoutBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}