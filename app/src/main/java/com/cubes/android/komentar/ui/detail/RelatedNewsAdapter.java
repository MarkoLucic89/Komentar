package com.cubes.android.komentar.ui.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.RelatedNewsViewHolder> {

    private ArrayList<News> newsList;
    private NewsListener listener;
    private int[] newsIdList;

    public RelatedNewsAdapter(ArrayList<News> newsList, NewsListener listener, int[] newsIdList) {
        this.newsList = newsList;
        this.listener = listener;
        this.newsIdList = newsIdList;
    }

    @NonNull
    @Override
    public RelatedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RelatedNewsViewHolder(
                RvItemCategorySmallBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedNewsViewHolder holder, int position) {
        News news = newsList.get(position);

        Picasso.get().load(news.image).into(holder.binding.imageView);
        holder.binding.textViewTitle.setText(news.title);
        holder.binding.textViewCategory.setText(news.category.name);
        holder.binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, news.url, this.newsIdList));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class RelatedNewsViewHolder extends RecyclerView.ViewHolder {

        private RvItemCategorySmallBinding binding;

        public RelatedNewsViewHolder(@NonNull RvItemCategorySmallBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
