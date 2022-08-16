package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import android.graphics.Color;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryTitleBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;

public class RvItemModelCategoryTitle implements ItemModelHome {

    private final String categoryTitle;
    private final String categoryColor;

    public RvItemModelCategoryTitle(String title, String color) {
        this.categoryTitle = title;
        this.categoryColor = color;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_home_category_title;
    }

    @Override
    public void bind(HomePagerAdapter.HomeViewHolder holder) {

        RvItemHomeCategoryTitleBinding binding = (RvItemHomeCategoryTitleBinding) holder.binding;

        binding.textViewTitle.setText(this.categoryTitle);
        binding.viewIndicator.setBackgroundColor(Color.parseColor(this.categoryColor));

        if (categoryTitle.equalsIgnoreCase("video")) {
            binding.getRoot().setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.blue_dark));
            binding.textViewTitle.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        } else {
            binding.getRoot().setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
            binding.textViewTitle.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.black));
        }


    }
}
