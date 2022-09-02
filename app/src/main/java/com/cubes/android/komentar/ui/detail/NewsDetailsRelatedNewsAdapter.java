package com.cubes.android.komentar.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsDetailsRelatedNewsAdapter extends RecyclerView.Adapter<NewsDetailsRelatedNewsAdapter.NewsDetailsRelatedNewsViewHolder> {

    private ArrayList<News> newsList;
    private NewsListener listener;
    private int[] newsIdList;

    public NewsDetailsRelatedNewsAdapter(ArrayList<News> newsList, NewsListener listener) {
        this.newsList = newsList;
        this.listener = listener;

        this.newsIdList = MyMethodsClass.initNewsIdList(newsList);
    }

    @NonNull
    @Override
    public NewsDetailsRelatedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsDetailsRelatedNewsViewHolder(
                RvItemCategorySmallBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDetailsRelatedNewsViewHolder holder, int position) {

        News news = newsList.get(position);

        RvItemCategorySmallBinding binding = (RvItemCategorySmallBinding) holder.binding;

        Picasso.get().load(news.image).into(binding.imageView);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.category.name);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, newsIdList));


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsDetailsRelatedNewsViewHolder extends RecyclerView.ViewHolder {

        private ViewBinding binding;

        public NewsDetailsRelatedNewsViewHolder(@NonNull ViewBinding viewBinding) {
            super(viewBinding.getRoot());

            binding = viewBinding;
        }
    }
}
