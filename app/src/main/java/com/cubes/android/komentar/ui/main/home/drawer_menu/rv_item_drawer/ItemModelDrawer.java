package com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerAdapter;

import java.util.Objects;

public interface ItemModelDrawer {

    int getType();

    void bind(DrawerAdapter.DrawerViewHolder holder);

    default int getId() {
        return -1;
    };


//    DiffUtil.ItemCallback<ItemModelDrawer> itemCallBack = new DiffUtil.ItemCallback<ItemModelDrawer>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull ItemModelDrawer oldItem, @NonNull ItemModelDrawer newItem) {
//            return oldItem.getId() == (newItem.getId());
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull ItemModelDrawer oldItem, @NonNull ItemModelDrawer newItem) {
//            return oldItem.getType() == newItem.getType();
//        }
//    };


}
