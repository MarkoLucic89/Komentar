package com.cubes.android.komentar.ui.main.videos.rv_model_videos;

import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;

public class RvItemModelVideoLoading implements ItemModelVideo {

    private NewsListener listener;

    public RvItemModelVideoLoading(NewsListener listener) {
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
