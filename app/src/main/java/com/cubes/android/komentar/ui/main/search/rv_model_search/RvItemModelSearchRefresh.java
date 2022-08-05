package com.cubes.android.komentar.ui.main.search.rv_model_search;

import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;

public class RvItemModelSearchRefresh implements ItemModelSearch {

    private LoadNextPageListener listener;

    public RvItemModelSearchRefresh(LoadNextPageListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void bind(SearchAdapter.SearchViewHolder holder) {
        RvItemRefreshBinding binding = (RvItemRefreshBinding) holder.binding;

        binding.getRoot().setOnClickListener(view -> {
            listener.loadNextPage();
        });
    }

}
