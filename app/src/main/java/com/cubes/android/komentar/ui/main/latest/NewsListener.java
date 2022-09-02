package com.cubes.android.komentar.ui.main.latest;


public interface NewsListener {

    //metode su default jer ne koristim svaku na svakom mestu gde implementiram interface

    //ova metoda sluzi za otvaranje DetailActivity-ja sa ViewPager-om
    default void onNewsClicked(int newsId, int[] newsIdList) {

    }

    default void loadNextPage() {

    }

}
