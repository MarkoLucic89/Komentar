package com.cubes.android.komentar.ui.main.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemHomeTabsNewsBinding;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class HomeTabsAdapter extends RecyclerView.Adapter<HomeTabsAdapter.HomeTabsViewHolder> {

    private ArrayList<News> newsList;

    public HomeTabsAdapter(ArrayList<News> newsList) {
        this.newsList = newsList;
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

        holder.binding.getRoot().setOnClickListener(view -> MyMethodsClass.goToNewsDetailActivity(view, news.id));

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
