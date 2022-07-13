package com.cubes.android.komentar.data.model.rv_item_model.rv_item_drawer;

import com.cubes.android.komentar.ui.adapter.DrawerAdapter;

public interface ItemModelDrawer {

    int getType();

    void bind(DrawerAdapter.DrawerViewHolder holder);
}
