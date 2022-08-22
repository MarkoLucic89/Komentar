package com.cubes.android.komentar.ui.main.home.home_pager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemHomeTabsNewsBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class HomeTabsAdapter extends RecyclerView.Adapter<HomeTabsAdapter.HomeTabsViewHolder> {

    private ArrayList<News> newsList;
    private NewsListener listener;

    private int[] newsIdList;

    public HomeTabsAdapter(ArrayList<News> newsList, NewsListener listener) {
        this.newsList = newsList;
        this.listener = listener;

        this.newsIdList = MyMethodsClass.initNewsIdList(newsList);
    }

    @NonNull
    @Override
    public HomeTabsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeTabsViewHolder(
                RvItemHomeTabsNewsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTabsViewHolder holder, int position) {

        News news = newsList.get(position);

        holder.binding.textViewTitle.setText(news.title);
        holder.binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, news.url, newsIdList));

    }

    @Override
    public int getItemCount() {
        if (newsList == null) {
            return 0;
        }
        return newsList.size();
    }

    public void updateNewsList(ArrayList<News> newsList) {

        this.newsList = newsList;

        if (newsList != null) {
            this.newsIdList = MyMethodsClass.initNewsIdList(newsList);
        }

        notifyDataSetChanged();
    }

    public class HomeTabsViewHolder extends RecyclerView.ViewHolder {

        private RvItemHomeTabsNewsBinding binding;

        public HomeTabsViewHolder(@NonNull RvItemHomeTabsNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
