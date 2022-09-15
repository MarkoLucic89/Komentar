package com.cubes.android.komentar.ui.main.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.search.rv_model_search.ItemModelSearch;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearch;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearchAd;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearchLoading;
import com.cubes.android.komentar.ui.main.search.rv_model_search.RvItemModelSearchRefresh;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<ItemModelSearch> itemModels = new ArrayList<>();
    private NewsListener listener;

    private int[] newsIdList;

    private int adsCounter = 0;

    public SearchAdapter(NewsListener listener) {
        this.listener = listener;
    }

    public void initList(ArrayList<News> newsList, boolean hasMorePages) {

        adsCounter = 0;

        itemModels.clear();

        newsIdList = MyMethodsClass.initNewsIdList(newsList);


        for (int i = 0; i < newsList.size(); i++) {

            if (i > 0 && i % 5 == 0 && adsCounter < 5) {

                itemModels.add(new RvItemModelSearchAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelSearch(newsList.get(i), listener, newsIdList));

        }

//        if (responseModel.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case R.layout.rv_item_category_small:
                binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_loading:
                binding = RvItemLoadingBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_refresh:
                binding = RvItemRefreshBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_ad:
                binding = RvItemAdBinding.inflate(inflater, parent, false);
                AdRequest adRequest = new AdRequest.Builder().build();
                ((RvItemAdBinding) binding).adView.loadAd(adRequest);
                break;
            default:
                binding = null;
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

    public void addNextPage(ArrayList<News> newsList, boolean hasMorePages) {

        if (newsList.isEmpty()) {
            itemModels.remove(itemModels.size() - 1);
            notifyItemRemoved(itemModels.size());
            return;
        }

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        newsIdList = MyMethodsClass.initNewsIdList(newsList);


        for (int i = 0; i < newsList.size(); i++) {

            if (i % 5 == 0 && adsCounter < 5) {

                itemModels.add(new RvItemModelSearchAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelSearch(newsList.get(i), listener, newsIdList));

        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

        notifyItemRangeInserted((lastIndex + 1), newsList.size());
    }

    public void refreshPage(ArrayList<News> newsList, boolean hasMorePages) {

        if (newsList.isEmpty()) {
            itemModels.remove(itemModels.size() - 1);
            notifyItemRemoved(itemModels.size());
            return;
        }

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        newsIdList = MyMethodsClass.initNewsIdList(newsList);


        for (int i = 0; i < newsList.size(); i++) {

            if (i % 5 == 0 && adsCounter < 5) {

                itemModels.add(new RvItemModelSearchAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelSearch(newsList.get(i), listener, newsIdList));

        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelSearchLoading(listener));
        }

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

