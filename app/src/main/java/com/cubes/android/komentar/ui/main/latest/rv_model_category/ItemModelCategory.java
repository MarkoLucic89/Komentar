package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;

public interface ItemModelCategory {

    int getType();

    void bind(CategoryAdapter.CategoryViewHolder holder);

    default void closeMenu() {

    }
}
