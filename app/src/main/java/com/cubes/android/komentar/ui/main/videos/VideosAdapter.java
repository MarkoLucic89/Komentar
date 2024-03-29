package com.cubes.android.komentar.ui.main.videos;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.databinding.RvItemLoadingVideosBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.ItemModelVideo;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideoLoading;
import com.cubes.android.komentar.ui.main.videos.rv_model_videos.RvItemModelVideos;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;

import java.util.ArrayList;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private ArrayList<ItemModelVideo> itemModels = new ArrayList<>();
    private boolean isOnHomePage;
    private LoadNextPageListener listener;

    public VideosAdapter(LoadNextPageListener listener) {
        this.isOnHomePage = false;
        this.listener = listener;
    }

    public VideosAdapter(ArrayList<News> newsList, boolean isOnHomePage) {
        this.isOnHomePage = isOnHomePage;
        initList(newsList);
    }

    private void initList(ArrayList<News> newsList) {
        this.itemModels = new ArrayList<>();

        for (News news : newsList) {
            itemModels.add(new RvItemModelVideos(news));
        }

        notifyDataSetChanged();
    }

    public void updateList(NewsResponseModel.NewsDataResponseModel responseModel) {

        for (News news : responseModel.news) {
            itemModels.add(new RvItemModelVideos(news));
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


        if (viewType == 0) {
            binding = RvItemVideosBinding.inflate(
                    inflater,
                    parent,
                    false
            );
        } else {
            binding = RvItemLoadingVideosBinding.inflate(
                    inflater,
                    parent,
                    false
            );
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

    public void loadNextPage(NewsResponseModel.NewsDataResponseModel response) {

        int lastIndex = itemModels.size() - 1;

        itemModels.remove(lastIndex);

        for (News news : response.news) {
            itemModels.add(new RvItemModelVideos(news));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelVideoLoading(listener));
//        }

        if (response.news.size() == 20) {
            itemModels.add(new RvItemModelVideoLoading(listener));
        }
        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted(lastIndex, newsList.size());


//        notifyDataSetChanged();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public VideosViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
