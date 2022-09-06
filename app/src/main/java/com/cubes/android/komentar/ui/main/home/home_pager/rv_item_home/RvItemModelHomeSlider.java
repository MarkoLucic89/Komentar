package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;


import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemHomeSliderBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.cubes.android.komentar.ui.main.home.home_pager.HomeSliderAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;

import java.util.ArrayList;

public class RvItemModelHomeSlider implements ItemModelHome{

    public ArrayList<News> newsList;
    public SnapHelper snapHelper;
    private boolean isEditorsChoice;
    private int newsPosition;

    private NewsListener listener;

    public RvItemModelHomeSlider(ArrayList<News> newsList, boolean showCategory, NewsListener listener) {
        this.newsList = newsList;
        this.snapHelper = new PagerSnapHelper();
        this.isEditorsChoice = showCategory;
        this.listener = listener;
    }

    public RvItemModelHomeSlider(ArrayList<News> newsList, boolean showCategory, NewsListener listener, int newsPosition) {
        this.newsList = newsList;
        this.snapHelper = new PagerSnapHelper();
        this.isEditorsChoice = showCategory;
        this.listener = listener;
        this.newsPosition = newsPosition;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_home_slider;
    }

    @Override
    public void bind(HomePagerAdapter.HomeViewHolder holder) {

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

        binding.viewPager.setAdapter(new HomeSliderAdapter(newsList, isEditorsChoice, listener, newsPosition));

        binding.progressBar.attachToRecyclerView(binding.viewPager, snapHelper);

    }
}
