package com.cubes.android.komentar.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_videos.ItemModelVideo;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_videos.RvItemModelVideos;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;

import java.util.ArrayList;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private ArrayList<ItemModelVideo> itemModels;
    private boolean isOnHomePage;

    public VideosAdapter(ArrayList<News> newsList) {
        this.isOnHomePage = false;
        initList(newsList);
    }

    public VideosAdapter(ArrayList<News> newsList, boolean isOnHomePage) {
        this.isOnHomePage = isOnHomePage;
        initList(newsList);
    }

    private void initList(ArrayList<News> newsList) {
        this.itemModels = new ArrayList<>();

        for (News news : newsList) {
            itemModels.add(new RvItemModelVideos(news));
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideosViewHolder(
                RvItemVideosBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {
        itemModels.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        if (isOnHomePage && itemModels.size() > 5) {
            return 5;
        }
        return itemModels.size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public VideosViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
