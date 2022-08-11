package com.cubes.android.komentar.ui.main.search.rv_model_search;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;

public class RvItemModelSearchRefresh implements ItemModelSearch {

    private NewsListener listener;

    public RvItemModelSearchRefresh(NewsListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_refresh;
    }

    @Override
    public void bind(SearchAdapter.SearchViewHolder holder) {
        RvItemRefreshBinding binding = (RvItemRefreshBinding) holder.binding;

        binding.getRoot().setOnClickListener(view -> {
            listener.loadNextPage();
        });
    }

}
