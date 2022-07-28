package com.cubes.android.komentar.ui.main.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.news_response.NewsResponseModel;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.search.rv_model_search.ItemModelSearch;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearch;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearchLoading;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<ItemModelSearch> itemModels;
    private LoadNextPageListener listener;

    public SearchAdapter() {
        itemModels = new ArrayList<>();

    }

    public SearchAdapter(ArrayList<News> newsList) {

        itemModels = new ArrayList<>();

        for (News news : newsList) {
            itemModels.add(new RvItemModelSearch(news, true));
        }
    }

    public SearchAdapter(NewsResponseModel responseModel, LoadNextPageListener listener) {

        updateList(responseModel, listener);

    }

    public void updateList(NewsResponseModel responseModel, LoadNextPageListener listener) {

        itemModels.clear();

        for (News news : responseModel.data.news) {
            itemModels.add(new RvItemModelSearch(news, true));
        }

        if (responseModel.data.pagination.has_more_pages) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

        notifyDataSetChanged();

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

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getType();
    }

    public void loadNextPage(NewsResponseModel response) {

        int lastIndex = itemModels.size() - 1;

        itemModels.remove(lastIndex);

        updateList(response.data.news);

        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted(lastIndex, newsList.size());


//        notifyDataSetChanged();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public SearchViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

