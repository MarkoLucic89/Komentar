package com.cubes.android.komentar.ui.main.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.TagResponseModel;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.search.rv_model_search.ItemModelSearch;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearch;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearchLoading;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearchRefresh;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideoRefresh;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<ItemModelSearch> itemModels = new ArrayList<>();
    private LoadNextPageListener listener;

    public SearchAdapter(LoadNextPageListener listener) {
        this.listener = listener;
    }

    public SearchAdapter(ArrayList<News> newsList) {

        itemModels = new ArrayList<>();

        for (News news : newsList) {
            itemModels.add(new RvItemModelSearch(news, true));
        }
    }


    public void updateList(NewsResponseModel.NewsDataResponseModel responseModel) {

        itemModels.clear();

        if (responseModel == null) {
            notifyDataSetChanged();
            return;
        }

        for (News news : responseModel.news) {
            itemModels.add(new RvItemModelSearch(news, true));
        }

//        if (responseModel.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (responseModel.news.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            binding = RvItemCategorySmallBinding.inflate(
                    inflater,
                    parent,
                    false);
        } else if (viewType == 1) {
            binding = RvItemLoadingBinding.inflate(
                    inflater,
                    parent,
                    false
            );
        } else {
            binding = RvItemRefreshBinding.inflate(
                    inflater,
                    parent,
                    false
            );
        }

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

    public void addNextPage(NewsResponseModel.NewsDataResponseModel response) {

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        for (News news : response.news) {
            itemModels.add(new RvItemModelSearch(news));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (response.news.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

//        notifyItemRangeChanged(lastIndex, itemModels.size());
        notifyItemRangeInserted((lastIndex + 1), response.news.size());


//        notifyDataSetChanged();
    }

    public void addNextPage(TagResponseModel.TagDataResponseModel response) {

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }


        for (News news : response.news) {
            itemModels.add(new RvItemModelSearch(news));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (response.news.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

//        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted((lastIndex + 1), response.news.size());

        notifyDataSetChanged();
    }

    public void clearList() {
        itemModels.clear();
        notifyDataSetChanged();
    }

    public void addRefresher() {

        if (itemModels.isEmpty()) {
            return;
        }

        itemModels.remove(itemModels.size() - 1);
        itemModels.add(new RvItemModelSearchRefresh(listener));

        notifyItemChanged(itemModels.size() - 1);

    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public SearchViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

