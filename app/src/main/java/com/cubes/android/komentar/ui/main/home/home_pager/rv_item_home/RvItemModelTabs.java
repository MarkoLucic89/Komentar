package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.HomePageData;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemHomeTabsBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.cubes.android.komentar.ui.main.home.home_pager.HomeTabsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;

import java.util.ArrayList;

public class RvItemModelTabs implements ItemModelHome {

    public ArrayList<News> latest;
    public ArrayList<News> mostRead;
    public ArrayList<News> mostComment;
    public int modelIndex;

    public int tabPosition = 0;
    public HomePagerAdapter homePagerAdapter;
    public HomeTabsAdapter homeTabsAdapter;
    public NewsListener listener;

    public RvItemModelTabs(HomePageData data, HomePagerAdapter adapter, int modelIndex, NewsListener listener) {
        this.latest = data.latest;
        this.mostRead = data.most_read;
        this.mostComment = data.most_commented;

        this.homePagerAdapter = adapter;
        this.modelIndex = modelIndex;

        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_home_tabs;
    }

    @Override
    public void bind(HomePagerAdapter.HomeViewHolder holder) {

        RvItemHomeTabsBinding binding = (RvItemHomeTabsBinding) holder.binding;

        initRecyclerView(binding);

        initUI(binding, holder);

        setListeners(binding);

    }

    private void setListeners(RvItemHomeTabsBinding binding) {

        binding.textViewLatest.setOnClickListener(view -> {
            tabPosition = 0;
            homePagerAdapter.notifyItemChanged(modelIndex);
//            homeTabsAdapter.updateNewsList(this.latest);
        });

        binding.textViewMostRead.setOnClickListener(view -> {
            tabPosition = 1;
            homePagerAdapter.notifyItemChanged(modelIndex);
//            homeTabsAdapter.updateNewsList(this.mostRead);
        });

        binding.textViewMostCommented.setOnClickListener(view -> {
            tabPosition = 2;
            homePagerAdapter.notifyItemChanged(modelIndex);
//            homeTabsAdapter.updateNewsList(this.mostComment);
        });
    }

    private void initRecyclerView(RvItemHomeTabsBinding binding) {

        homeTabsAdapter = new HomeTabsAdapter(this.latest, listener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.recyclerView.getContext()));
        binding.recyclerView.setAdapter(homeTabsAdapter);

    }

    private void initUI(RvItemHomeTabsBinding binding, HomePagerAdapter.HomeViewHolder holder) {

        binding.viewIndicatorLatest.setVisibility(View.GONE);
        binding.viewIndicatorMostRead.setVisibility(View.GONE);
        binding.viewIndicatorMostComment.setVisibility(View.GONE);

        binding.textViewLatest.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.grey_light));
        binding.textViewMostRead.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.grey_light));
        binding.textViewMostCommented.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.grey_light));

        if (tabPosition == 0) {
            binding.viewIndicatorLatest.setVisibility(View.VISIBLE);
            binding.textViewLatest.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            homeTabsAdapter.updateNewsList(this.latest);

        } else if (tabPosition == 1) {
            binding.viewIndicatorMostRead.setVisibility(View.VISIBLE);
            binding.textViewMostRead.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            homeTabsAdapter.updateNewsList(this.mostRead);

        } else {
            binding.viewIndicatorMostComment.setVisibility(View.VISIBLE);
            binding.textViewMostCommented.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            homeTabsAdapter.updateNewsList(this.mostComment);

        }

    }
}
