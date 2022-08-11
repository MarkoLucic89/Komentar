package com.cubes.android.komentar.ui.main.latest;


import com.cubes.android.komentar.data.model.News;

import java.util.ArrayList;

public interface NewsListener {

    //metode su default jer ne koristim svaku na svakom mestu gde implementiram interface

    //ovo ce verovatno da se obrise (sluzilo je za NewsDetailActivity bez ViewPager-a)
    default void onNewsClicked(int newsId) {

    }

    //ova metoda sluzi za otvaranje DetailActivity-ja sa ViewPager-om
    default void onNewsClicked(int newsId, String newsUrl, ArrayList<News> news) {

    }

    default void loadNextPage() {

    }
}
