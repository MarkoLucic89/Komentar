package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;

public class RvItemModelCategoryRefresh implements ItemModelCategory {

    private LoadNextPageListener listener;

    public RvItemModelCategoryRefresh(LoadNextPageListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public void bind(CategoryAdapter.CategoryViewHolder holder) {

        RvItemRefreshBinding binding = (RvItemRefreshBinding) holder.binding;

        binding.getRoot().setOnClickListener(view -> {
            listener.loadNextPage();
        });


    }
}
