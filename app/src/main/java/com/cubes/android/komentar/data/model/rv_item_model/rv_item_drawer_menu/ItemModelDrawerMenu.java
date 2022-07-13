package com.cubes.android.komentar.data.model.rv_item_model.rv_item_drawer_menu;

import com.cubes.android.komentar.ui.adapter.DrawerMenuAdapter;

public interface ItemModelDrawerMenu {

    int getType();

    void bind(DrawerMenuAdapter.DrawerMenuViewHolder holder);
}
