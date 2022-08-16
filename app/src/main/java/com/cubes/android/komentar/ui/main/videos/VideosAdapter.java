package com.cubes.android.komentar.ui.main.videos;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.databinding.RvItemLoadingVideosBinding;
import com.cubes.android.komentar.databinding.RvItemRefreshVideosBinding;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.ItemModelVideo;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideoLoading;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideoRefresh;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideos;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private ArrayList<ItemModelVideo> itemModels = new ArrayList<>();
    private boolean isOnHomePage;
    private NewsListener listener;

    private int[] newsIdList;

    public VideosAdapter(NewsListener listener) {
        this.isOnHomePage = false;
        this.listener = listener;
    }

    public VideosAdapter(ArrayList<News> newsList, boolean isOnHomePage, NewsListener listener) {

        this.isOnHomePage = isOnHomePage;
        this.listener = listener;

        newsIdList = MyMethodsClass.initNewsIdList(newsList);

        for (News news : newsList) {
            itemModels.add(new RvItemModelVideos(news, listener, newsIdList));
        }

    }


    public void updateList(NewsResponseModel.NewsDataResponseModel responseModel) {

        newsIdList = MyMethodsClass.initNewsIdList(responseModel.news);

        for (News news : responseModel.news) {
            itemModels.add(new RvItemModelVideos(news, listener, newsIdList));
        }

        if (responseModel.pagination.has_more_pages) {
            itemModels.add(new RvItemModelVideoLoading(listener));
        }

        notifyDataSetChanged();

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

    public void addNextPage(NewsResponseModel.NewsDataResponseModel response) {

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        newsIdList = MyMethodsClass.initNewsIdList(response.news);

        for (News news : response.news) {
            itemModels.add(new RvItemModelVideos(news, listener, newsIdList));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelVideoLoading(listener));
//        }

        if (response.news.size() == 20) {
            itemModels.add(new RvItemModelVideoLoading(listener));
        }

//        notifyItemRangeChanged(lastIndex, itemModels.size());
        notifyItemRangeInserted(lastIndex + 1, response.news.size());
//        notifyDataSetChanged();
    }

    public void addRefresher() {

        if (itemModels.isEmpty()) {
            return;
        }

        itemModels.remove(itemModels.size() - 1);
        itemModels.add(new RvItemModelVideoRefresh(listener));

        notifyItemChanged(itemModels.size() - 1);

    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public VideosViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
