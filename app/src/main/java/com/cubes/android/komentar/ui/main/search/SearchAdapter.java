package com.cubes.android.komentar.ui.main.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.news_response.NewsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.tag_response.TagResponseModel;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.search.rv_model_search.ItemModelSearch;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearch;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearchLoading;

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


    public void updateList(NewsResponseModel responseModel) {

        itemModels.clear();

        if (responseModel == null) {
            notifyDataSetChanged();
            return;
        }

        for (News news : responseModel.data.news) {
            itemModels.add(new RvItemModelSearch(news, true));
        }

//        if (responseModel.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (responseModel.data.news.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

        notifyDataSetChanged();

    }

    public void updateList(TagResponseModel responseModel) {

        itemModels.clear();

        if (responseModel == null) {
            notifyDataSetChanged();
            return;
        }

        for (News news : responseModel.data.news) {
            itemModels.add(new RvItemModelSearch(news, true));
        }

//        if (responseModel.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (responseModel.data.news.size() == 20) {
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
        } else {
            binding = RvItemLoadingBinding.inflate(
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

    public void loadNextPage(NewsResponseModel response) {

        int lastIndex = itemModels.size() - 1;

        itemModels.remove(lastIndex);

        for (News news : response.data.news) {
            itemModels.add(new RvItemModelSearch(news));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (response.data.news.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted(lastIndex, newsList.size());


//        notifyDataSetChanged();
    }

    public void loadNextPage(TagResponseModel response) {

        int lastIndex = itemModels.size() - 1;

        itemModels.remove(lastIndex);

        for (News news : response.data.news) {
            itemModels.add(new RvItemModelSearch(news));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (response.data.news.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted(lastIndex, newsList.size());


//        notifyDataSetChanged();
    }

    public void clearList() {
        itemModels.clear();
        notifyDataSetChanged();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public SearchViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

