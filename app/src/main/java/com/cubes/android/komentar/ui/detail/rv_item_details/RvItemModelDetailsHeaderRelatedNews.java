package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.graphics.Color;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryTitleBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.ItemModelHome;

public class RvItemModelDetailsHeaderRelatedNews implements ItemModelDetails {

    public RvItemModelDetailsHeaderRelatedNews() {
    }

    @Override
    public int getType() {
        return R.layout.rv_item_home_category_title;
    }


    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemHomeCategoryTitleBinding binding = (RvItemHomeCategoryTitleBinding) holder.binding;

        binding.textViewTitle.setText(R.string.povezane_vesti);
        binding.viewIndicator.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.blue_dark));

        binding.getRoot().setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.textViewTitle.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.black));


    }
}
