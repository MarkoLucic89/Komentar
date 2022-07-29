package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;

public class RvItemModelCategoryLoading implements ItemModelCategory {

    private LoadNextPageListener listener;

    public RvItemModelCategoryLoading(LoadNextPageListener listener) {
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
