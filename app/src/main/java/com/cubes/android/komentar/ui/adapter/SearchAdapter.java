package com.cubes.android.komentar.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_search.ItemModelSearch;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_search.RvItemModelSearch;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<ItemModelSearch> itemModels;
    private ArrayList<News> newsList;

    public SearchAdapter() {
        itemModels = new ArrayList<>();

    }

    public SearchAdapter(ArrayList<News> newsList) {

        itemModels = new ArrayList<>();
        this.newsList = newsList;

        for (News news : newsList) {
            itemModels.add(new RvItemModelSearch(news, true));
        }
    }

    public void updateList(ArrayList<News> newsList) {
        itemModels.clear();

        if (newsList.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        for (int i = 0; i < newsList.size(); i++) {
            itemModels.add(new RvItemModelSearch(newsList.get(i)));
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding = RvItemCategorySmallBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new SearchAdapter.SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        itemModels.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public SearchViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

