package com.cubes.android.komentar.ui.main.videos.rv_model_videos;


import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;

public interface ItemModelVideo {

    int getType();

    default News getNews() {
        return null;
    }

    void bind(VideosAdapter.VideosViewHolder holder);

    default void closeMenu() {

    }

}
