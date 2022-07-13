package com.cubes.android.komentar.data.model.rv_item_model.rv_item_home;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemHomeVideoBinding;
import com.cubes.android.komentar.ui.adapter.HomeAdapter;
import com.cubes.android.komentar.ui.adapter.VideosAdapter;

import java.util.ArrayList;

public class RvItemModelHomeVideo implements ItemModelHome {

    public ArrayList<News> newsList;

    public RvItemModelHomeVideo(ArrayList<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public void bind(HomeAdapter.HomeViewHolder holder) {

        RvItemHomeVideoBinding binding = (RvItemHomeVideoBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(new VideosAdapter(newsList, true));

    }
}
