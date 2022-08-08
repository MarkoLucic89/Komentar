package com.cubes.android.komentar.ui.main.home.rv_item_home;

import android.graphics.Color;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.HomePageResponseModel;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryBoxBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.home.HomeAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;

import java.util.ArrayList;

public class RvItemModelCategoryBox implements ItemModelHome {

    private final String categoryTitle;
    private final String categoryColor;
    private final ArrayList<News> list;
    private final NewsListener listener;

    public RvItemModelCategoryBox(HomePageResponseModel.CategoryBoxResponseModel category, NewsListener listener) {
        this.categoryTitle = category.title;
        this.categoryColor = category.color;
        this.list = category.news;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public void bind(HomeAdapter.HomeViewHolder holder) {

        RvItemHomeCategoryBoxBinding binding = (RvItemHomeCategoryBoxBinding) holder.binding;

        binding.textViewTitle.setText(this.categoryTitle);
        binding.viewIndicator.setBackgroundColor(Color.parseColor(this.categoryColor));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.recyclerView.getContext()));
        binding.recyclerView.setAdapter(new CategoryAdapter(this.list, true, listener));
    }
}
