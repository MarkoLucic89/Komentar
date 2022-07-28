package com.cubes.android.komentar.ui.main.videos.rv_model_videos;


import com.cubes.android.komentar.ui.main.videos.VideosAdapter;

public interface ItemModelVideo {

    int getType();

    void bind(VideosAdapter.VideosViewHolder holder);

}
