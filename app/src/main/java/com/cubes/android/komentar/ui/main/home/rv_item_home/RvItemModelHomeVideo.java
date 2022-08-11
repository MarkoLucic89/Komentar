package com.cubes.android.komentar.ui.main.home.rv_item_home;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemHomeVideoBinding;
import com.cubes.android.komentar.ui.main.home.HomeAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;

import java.util.ArrayList;

public class RvItemModelHomeVideo implements ItemModelHome {

    public ArrayList<News> newsList;
    public NewsListener listener;

    public RvItemModelHomeVideo(ArrayList<News> newsList, NewsListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_home_video;
    }

    @Override
    public void bind(HomeAdapter.HomeViewHolder holder) {

        RvItemHomeVideoBinding binding = (RvItemHomeVideoBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(new VideosAdapter(newsList, true, listener));

    }
}
