package com.cubes.android.komentar.ui.main.home.rv_item_home;

import com.cubes.android.komentar.ui.main.home.HomeAdapter;

public interface ItemModelHome {

    int getType();

    void bind(HomeAdapter.HomeViewHolder holder);
}
