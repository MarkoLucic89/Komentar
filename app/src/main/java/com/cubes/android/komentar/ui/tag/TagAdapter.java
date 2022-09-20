package com.cubes.android.komentar.ui.tag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.source.remote.networking.response.TagResponseModel;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.ItemModelCategory;
import com.cubes.android.komentar.ui.tag.rv_model_tag.ItemModelTag;
import com.cubes.android.komentar.ui.tag.rv_model_tag.RvItemModelTag;
import com.cubes.android.komentar.ui.tag.rv_model_tag.RvItemModelTagAd;
import com.cubes.android.komentar.ui.tag.rv_model_tag.RvItemModelTagLoading;
import com.cubes.android.komentar.ui.tag.rv_model_tag.RvItemModelTagRefresh;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private ArrayList<ItemModelTag> itemModels = new ArrayList<>();
    private NewsListener listener;
    private int adsCounter = 0;

    private int[] newsIdList;

    public TagAdapter(NewsListener listener) {
        this.listener = listener;
    }

    public void initList(ArrayList<News> newsList, boolean hasMorePages) {

        adsCounter = 0;

        itemModels.clear();

        newsIdList = MyMethodsClass.initNewsIdList(newsList);

        for (int i = 0; i < newsList.size(); i++) {

            if (i > 0 && i % 5 == 0 && adsCounter < 5) {

                itemModels.add(new RvItemModelTagAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelTag(newsList.get(i), listener, newsIdList));

        }

//        if (hasMorePages) {
//            itemModels.add(new RvItemModelTagLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelTagLoading(listener));
        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public TagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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

                ((RvItemAdBinding) binding).shimmerLayout.setVisibility(View.VISIBLE);
                ((RvItemAdBinding) binding).shimmerLayout.startLayoutAnimation();
                ((RvItemAdBinding) binding).adView.setVisibility(View.GONE);

                AdRequest adRequest = new AdRequest.Builder().build();
                ((RvItemAdBinding) binding).adView.loadAd(adRequest);

                ((RvItemAdBinding) binding).adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                        ((RvItemAdBinding) binding).shimmerLayout.stopShimmer();
                        ((RvItemAdBinding) binding).shimmerLayout.setVisibility(View.GONE);
                        ((RvItemAdBinding) binding).adView.setVisibility(View.VISIBLE);

                    }
                });

                break;
            default:
                binding = null;
        }

        return new TagAdapter.TagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagViewHolder holder, int position) {
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

                itemModels.add(new RvItemModelTagAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelTag(newsList.get(i), listener, newsIdList));

        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelTagLoading(listener));
        }

        notifyItemRangeInserted((lastIndex + 1), newsList.size());
    }

    public void refreshList(ArrayList<News> newsList, boolean hasMorePages) {

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

                itemModels.add(new RvItemModelTagAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelTag(newsList.get(i), listener, newsIdList));

        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelTagLoading(listener));
        }

        notifyDataSetChanged();
    }

    public void addRefresher() {

        if (itemModels.isEmpty()) {
            return;
        }

        itemModels.remove(itemModels.size() - 1);
        itemModels.add(new RvItemModelTagRefresh(listener));

        notifyItemChanged(itemModels.size() - 1);

    }

    public void closeAllMenus() {
        for (ItemModelTag itemModelTag : itemModels) {
            itemModelTag.closeMenu();
        }
    }


    public class TagViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public TagViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}


