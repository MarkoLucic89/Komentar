package com.cubes.android.komentar.ui.main.latest;


import com.cubes.android.komentar.data.model.News;

import java.util.ArrayList;

public interface NewsListener {

    void onNewsClicked(int newsId);

    default void onNewsClicked(int newsId, String newsUrl, ArrayList<News> news) {

    }

    default void loadNextPage() {

    }
}
