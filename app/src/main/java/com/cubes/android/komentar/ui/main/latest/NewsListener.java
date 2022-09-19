package com.cubes.android.komentar.ui.main.latest;


import com.cubes.android.komentar.data.model.domain.News;

public interface NewsListener {

    //metode su default jer ne koristim svaku na svakom mestu gde implementiram interface

    //ova metoda sluzi za otvaranje DetailActivity-ja sa HomePage-a
    default void onNewsClicked(int newsId) {

    }

    //ova metoda sluzi za otvaranje DetailActivity-ja iz ostalih ekrana
    default void onNewsClicked(int newsId, int[] newsIdList) {

    }

    default void onNewsMenuShareClicked(String url) {

    }

    default void onNewsMenuCommentsClicked(int newsId) {

    }

    default void onNewsMenuFavoritesClicked(News news) {

    }

    default void loadNextPage() {

    }

    default void refreshPage() {

    }

    default void closeOtherMenus() {

    }

}
