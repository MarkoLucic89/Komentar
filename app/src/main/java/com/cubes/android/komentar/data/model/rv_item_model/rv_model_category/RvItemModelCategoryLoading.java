package com.cubes.android.komentar.data.model.rv_item_model.rv_model_category;

import com.cubes.android.komentar.listeners.UpdateCategoryListListener;
import com.cubes.android.komentar.ui.adapter.CategoryAdapter;

public class RvItemModelCategoryLoading implements ItemModelCategory {

    private CategoryAdapter adapter;
    private UpdateCategoryListListener listener;

    public RvItemModelCategoryLoading(CategoryAdapter adapter, UpdateCategoryListListener listener) {
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void bind(CategoryAdapter.CategoryViewHolder holder) {
        listener.loadNextPage();
    }
}
