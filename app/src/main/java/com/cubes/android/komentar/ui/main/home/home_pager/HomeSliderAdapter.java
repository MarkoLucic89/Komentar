package com.cubes.android.komentar.ui.main.home.home_pager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.VpItemHomeSliderBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeSliderAdapter extends RecyclerView.Adapter<HomeSliderAdapter.HomeSliderViewHolder> {

    private ArrayList<News> list;
    private boolean showCategory;

    private NewsListener listener;


    public HomeSliderAdapter(ArrayList<News> newsList, boolean showCategory, NewsListener listener) {
        this.list = newsList;
        this.showCategory = showCategory;
        this.listener = listener;
    }

    public HomeSliderAdapter(boolean b, NewsListener listener) {
        this.showCategory = showCategory;
        this.listener = listener;
    }

    public void updateList(ArrayList<News> newsArrayList) {
        this.list = newsArrayList;
        notifyDataSetChanged();
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
            holder.binding.textViewCategory.setText(news.categoryName);
            holder.binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));
        } else {
            holder.binding.layoutCategoryTime.setVisibility(View.GONE);
        }

        Picasso.get().load(news.image).into(holder.binding.imageView);
        holder.binding.textViewTitle.setText(news.title);

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id));

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
