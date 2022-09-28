package com.cubes.android.komentar.ui.main.videos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.databinding.RvItemLoadingVideosBinding;
import com.cubes.android.komentar.databinding.RvItemRefreshVideosBinding;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.ItemModelCategory;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.ItemModelVideo;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideoAd;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideoLoading;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideoRefresh;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideos;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private ArrayList<ItemModelVideo> itemModels = new ArrayList<>();
    private boolean isOnHomePage;
    private NewsListener listener;

    private int adsCounter = 0;

    private int[] newsIdList;

    public VideosAdapter(NewsListener listener) {
        this.isOnHomePage = false;
        this.listener = listener;
    }

    public VideosAdapter(ArrayList<News> newsList, boolean isOnHomePage, NewsListener listener) {

        this.isOnHomePage = isOnHomePage;
        this.listener = listener;

        this.newsIdList = MyMethodsClass.initNewsIdList(newsList);

        for (News news : newsList) {
            itemModels.add(new RvItemModelVideos(news, listener, newsIdList));
        }

    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case R.layout.rv_item_videos:
                binding = RvItemVideosBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_loading_videos:
                binding = RvItemLoadingVideosBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_refresh_videos:
                binding = RvItemRefreshVideosBinding.inflate(inflater, parent, false);
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

        return new VideosViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {
        itemModels.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        if (isOnHomePage && itemModels.size() > 5) {
            return 5;
        }
        return itemModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getType();
    }


    public void initList(ArrayList<News> newsList, boolean hasNextPage) {

        this.newsIdList = MyMethodsClass.initNewsIdList(newsList);

        adsCounter = 0;

        itemModels.clear();

        for (int i = 0; i < newsList.size(); i++) {

            if (i > 0 && i % 5 == 0 && adsCounter < 5) {

                itemModels.add(new RvItemModelVideoAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelVideos(newsList.get(i), listener, newsIdList));
        }

//        if (hasNextPage) {
//            itemModels.add(new RvItemModelVideoLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelVideoLoading(listener));
        }

        notifyDataSetChanged();

    }

    public void addNextPage(ArrayList<News> newsList, boolean hasNextPage) {

        if (newsList.isEmpty()) {

            itemModels.remove(itemModels.size() - 1);
            notifyItemRemoved(itemModels.size());
            return;

        }

        this.newsIdList = MyMethodsClass.initNewsIdList(newsList);

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        for (int i = 0; i < newsList.size(); i++) {

            if (i % 5 == 0 && adsCounter < 5) {

                itemModels.add(new RvItemModelVideoAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelVideos(newsList.get(i), listener, newsIdList));

        }

//        if (hasNextPage) {
//            itemModels.add(new RvItemModelVideoLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelVideoLoading(listener));
        }

        notifyItemRangeInserted(lastIndex + 1, newsList.size());
    }


    public void refreshPage(ArrayList<News> newsList, boolean hasNextPage) {

        if (newsList.isEmpty()) {

            itemModels.remove(itemModels.size() - 1);
            notifyItemRemoved(itemModels.size());
            return;

        }

        this.newsIdList = MyMethodsClass.initNewsIdList(newsList);

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        for (int i = 0; i < newsList.size(); i++) {

            if (i % 5 == 0 && adsCounter < 5) {

                itemModels.add(new RvItemModelVideoAd());
                adsCounter++;
            }

            itemModels.add(new RvItemModelVideos(newsList.get(i), listener, newsIdList));

        }

//        if (hasNextPage) {
//            itemModels.add(new RvItemModelVideoLoading(listener));
//        }

        if (newsList.size() >= 20) {
            itemModels.add(new RvItemModelVideoLoading(listener));
        }

        notifyDataSetChanged();
    }

    public void addRefresher() {

        if (itemModels.isEmpty()) {
            return;
        }

        itemModels.remove(itemModels.size() - 1);
        itemModels.add(new RvItemModelVideoRefresh(listener));

        notifyItemChanged(itemModels.size() - 1);

    }


    public void closeAllMenus() {
        for (ItemModelVideo itemModelVideo : itemModels) {
            itemModelVideo.closeMenu();
        }
    }

    public void updateBookmarks(int mTempNewsId, News bookmark) {

        for (ItemModelVideo model : itemModels) {
            if (model.getNews() != null && model.getNews().id == mTempNewsId) {
                if (bookmark == null) {
                    model.getNews().isInBookmarks = false;
                } else {
                    model.getNews().isInBookmarks = true;
                }
            }
        }

        notifyDataSetChanged();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public VideosViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
