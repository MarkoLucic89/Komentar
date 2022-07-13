package com.cubes.android.komentar.data.model.rv_item_model.rv_item_home;

import android.graphics.Color;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.response.home_response.CategoryBoxResponseModel;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryBoxBinding;
import com.cubes.android.komentar.ui.adapter.CategoryAdapter;
import com.cubes.android.komentar.ui.adapter.HomeAdapter;

import java.util.ArrayList;

public class RvItemModelCategoryBox implements ItemModelHome {

    public String categoryTitle;
    private String categoryColor;
    private ArrayList<News> list;

    public RvItemModelCategoryBox(CategoryBoxResponseModel category) {
        this.categoryTitle = category.title;
        this.categoryColor = category.color;
        this.list = category.news;
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
        binding.recyclerView.setAdapter(new CategoryAdapter(this.list, true));
    }
}
