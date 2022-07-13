package com.cubes.android.komentar.data.model.rv_item_model.rv_model_videos;


import com.cubes.android.komentar.ui.adapter.VideosAdapter;

public interface ItemModelVideo {

    int getType();

    void bind(VideosAdapter.VideosViewHolder holder);

}
