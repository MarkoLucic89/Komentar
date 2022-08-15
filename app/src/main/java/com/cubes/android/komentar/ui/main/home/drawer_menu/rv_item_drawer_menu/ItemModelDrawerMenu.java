package com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer_menu;

import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerMenuAdapter;

public interface ItemModelDrawerMenu {

    int getType();

    void bind(DrawerMenuAdapter.DrawerMenuViewHolder holder);
}
