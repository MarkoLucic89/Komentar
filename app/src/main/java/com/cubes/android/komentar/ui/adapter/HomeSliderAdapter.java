package com.cubes.android.komentar.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.VpItemHomeSliderBinding;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeSliderAdapter extends RecyclerView.Adapter<HomeSliderAdapter.HomeSliderViewHolder> {

    private ArrayList<News> list;
    private boolean showCategory;

    public HomeSliderAdapter(ArrayList<News> list) {
        this.list = list;
    }

    public HomeSliderAdapter(ArrayList<News> list, boolean showCategory) {
        this.list = list;
        this.showCategory = showCategory;
    }

    @NonNull
    @Override
    public HomeSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new HomeSliderViewHolder(
                VpItemHomeSliderBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSliderViewHolder holder, int position) {
        News news = list.get(position);

        if (showCategory) {
            holder.binding.layoutCategoryTime.setVisibility(View.VISIBLE);
            holder.binding.textViewCategory.setText(news.category.name);
            holder.binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));
        } else {
            holder.binding.layoutCategoryTime.setVisibility(View.GONE);
        }

        Picasso.get().load(news.image).into(holder.binding.imageView);
        holder.binding.textViewTitle.setText(news.title);

        holder.binding.getRoot().setOnClickListener(view -> MyMethodsClass.goToNewsDetailActivity(view, news.id));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class HomeSliderViewHolder extends RecyclerView.ViewHolder {

        private VpItemHomeSliderBinding binding;

        public HomeSliderViewHolder(@NonNull VpItemHomeSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
