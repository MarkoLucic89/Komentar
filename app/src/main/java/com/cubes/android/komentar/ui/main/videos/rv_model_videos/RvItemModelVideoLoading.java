package com.cubes.android.komentar.ui.main.videos.rv_model_videos;

import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;

public class RvItemModelVideoLoading implements ItemModelVideo {

    private LoadNextPageListener listener;

    public RvItemModelVideoLoading(LoadNextPageListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void bind(VideosAdapter.VideosViewHolder holder) {
        listener.loadNextPage();
    }

}
