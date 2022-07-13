package com.cubes.android.komentar.data.model.rv_item_model.rv_model_category;

import com.cubes.android.komentar.ui.adapter.CategoryAdapter;

public interface ItemModelCategory {

    int getType();

    void bind(CategoryAdapter.CategoryViewHolder holder);


}
