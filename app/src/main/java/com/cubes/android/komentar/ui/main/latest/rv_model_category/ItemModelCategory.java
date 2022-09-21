package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;

public interface ItemModelCategory {

    int getType();

    default News getNews() {
        return null;
    }

    default int getPosition() {
        return -1;
    }

    void bind(CategoryAdapter.CategoryViewHolder holder);

    default void closeMenu() {

    }
}
