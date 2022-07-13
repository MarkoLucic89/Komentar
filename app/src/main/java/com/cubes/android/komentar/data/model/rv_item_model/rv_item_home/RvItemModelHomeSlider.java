package com.cubes.android.komentar.data.model.rv_item_model.rv_item_home;


import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemHomeSliderBinding;
import com.cubes.android.komentar.ui.adapter.HomeAdapter;
import com.cubes.android.komentar.ui.adapter.HomeSliderAdapter;

import java.util.ArrayList;

public class RvItemModelHomeSlider implements ItemModelHome{

    public ArrayList<News> newsList;
    public boolean isSnapHelperAttached;
    public SnapHelper snapHelper;
    private boolean isEditorsChoice;

    public RvItemModelHomeSlider(ArrayList<News> newsList) {
        this.newsList = newsList;
        this.snapHelper = new PagerSnapHelper();
        this.isSnapHelperAttached = false;
        this.isEditorsChoice = false;
    }

    public RvItemModelHomeSlider(ArrayList<News> newsList, boolean showCategory) {
        this.newsList = newsList;
        this.snapHelper = new PagerSnapHelper();
        this.isSnapHelperAttached = false;
        this.isEditorsChoice = showCategory;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(HomeAdapter.HomeViewHolder holder) {

        RvItemHomeSliderBinding binding = (RvItemHomeSliderBinding) holder.binding;

        if (isEditorsChoice) {
            binding.textViewTitle.setText("Izbor urednika");
            binding.viewIndicator.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.red));
            binding.layoutHeader.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHeader.setVisibility(View.GONE);
        }

        binding.viewPager.setLayoutManager(new LinearLayoutManager(
                holder.itemView.getContext(),
                RecyclerView.HORIZONTAL,
                false
        ));

        binding.viewPager.setOnFlingListener(null);

        snapHelper.attachToRecyclerView(binding.viewPager);

        binding.viewPager.setAdapter(new HomeSliderAdapter(newsList, isEditorsChoice));

        binding.progressBar.attachToRecyclerView(binding.viewPager, snapHelper);

    }
}
