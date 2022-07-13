package com.cubes.android.komentar.data.model.rv_item_model.rv_item_home;

import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.ui.adapter.HomeAdapter;

public interface ItemModelHome {

    int getType();

    void bind(HomeAdapter.HomeViewHolder holder);
}
