package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;

public class RvItemModelCategoryRefresh implements ItemModelCategory {

    private NewsListener listener;

    public RvItemModelCategoryRefresh(NewsListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_refresh;
    }

    @Override
    public void bind(CategoryAdapter.CategoryViewHolder holder) {

        RvItemRefreshBinding binding = (RvItemRefreshBinding) holder.binding;

        binding.getRoot().setOnClickListener(view -> {
            listener.loadNextPage();
        });


    }
}
