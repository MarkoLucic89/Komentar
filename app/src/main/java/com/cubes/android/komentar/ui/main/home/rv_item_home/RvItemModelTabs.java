package com.cubes.android.komentar.ui.main.home.rv_item_home;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.HomePageResponseModel;
import com.cubes.android.komentar.databinding.RvItemHomeTabsBinding;
import com.cubes.android.komentar.ui.main.home.HomeAdapter;
import com.cubes.android.komentar.ui.main.home.HomeTabsAdapter;

import java.util.ArrayList;

public class RvItemModelTabs implements ItemModelHome {

    public ArrayList<News> latest;
    public ArrayList<News> mostRead;
    public ArrayList<News> mostComment;
    public int modelIndex;

    public int tabPosition = 0;
    public HomeAdapter homeAdapter;
    public HomeTabsAdapter homeTabsAdapter;

    public RvItemModelTabs(HomePageResponseModel.HomePageDataResponseModel data, HomeAdapter adapter, int modelIndex) {
        this.latest = data.latest;
        this.mostRead = data.most_read;
        this.mostComment = data.most_comment;

        this.homeAdapter = adapter;
        this.modelIndex = modelIndex;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void bind(HomeAdapter.HomeViewHolder holder) {

        RvItemHomeTabsBinding binding = (RvItemHomeTabsBinding) holder.binding;

        initRecyclerView(binding);

        initUI(binding, holder);

        setListeners(binding);

    }

    private void setListeners(RvItemHomeTabsBinding binding) {

        binding.textViewLatest.setOnClickListener(view -> {
            tabPosition = 0;
            homeAdapter.notifyItemChanged(modelIndex);
//            homeTabsAdapter.updateNewsList(this.latest);
        });

        binding.textViewMostRead.setOnClickListener(view -> {
            tabPosition = 1;
            homeAdapter.notifyItemChanged(modelIndex);
//            homeTabsAdapter.updateNewsList(this.mostRead);
        });

        binding.textViewMostCommented.setOnClickListener(view -> {
            tabPosition = 2;
            homeAdapter.notifyItemChanged(modelIndex);
//            homeTabsAdapter.updateNewsList(this.mostComment);
        });
    }

    private void initRecyclerView(RvItemHomeTabsBinding binding) {

        homeTabsAdapter = new HomeTabsAdapter(this.latest);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.recyclerView.getContext()));
        binding.recyclerView.setAdapter(homeTabsAdapter);

    }

    private void initUI(RvItemHomeTabsBinding binding, HomeAdapter.HomeViewHolder holder) {
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
