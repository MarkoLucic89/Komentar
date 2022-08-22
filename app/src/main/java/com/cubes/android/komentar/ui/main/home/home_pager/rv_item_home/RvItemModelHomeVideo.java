package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemHomeVideoBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;

import java.util.ArrayList;

public class RvItemModelHomeVideo implements ItemModelHome {

    private ArrayList<News> newsList;
    private NewsListener listener;

    public RvItemModelHomeVideo(ArrayList<News> newsList, NewsListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_home_video;
    }

    @Override
    public void bind(HomePagerAdapter.HomeViewHolder holder) {

        RvItemHomeVideoBinding binding = (RvItemHomeVideoBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(new VideosAdapter(newsList, true, listener));

    }
}
