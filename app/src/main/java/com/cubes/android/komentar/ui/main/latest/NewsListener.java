package com.cubes.android.komentar.ui.main.latest;


public interface NewsListener {

    void onNewsClicked(int newsId);

    default void loadNextPage() {

    }
}
