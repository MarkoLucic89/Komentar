package com.cubes.android.komentar.ui.main.drawer_menu.rv_item_drawer;

import com.cubes.android.komentar.ui.main.drawer_menu.DrawerAdapter;

public interface ItemModelDrawer {

    int getType();

    void bind(DrawerAdapter.DrawerViewHolder holder);
}
