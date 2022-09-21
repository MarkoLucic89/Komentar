package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;

public interface ItemModelHome {

    int getType();

    default News getNews() {
        return null;
    }

    void bind(HomePagerAdapter.HomeViewHolder holder);

    default void closeMenu() {

    }
}
