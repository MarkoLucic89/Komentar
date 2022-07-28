package com.cubes.android.komentar.ui.main.search.rv_model_search;

import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;

public class RvItemModelSearchLoading implements ItemModelSearch {

    private LoadNextPageListener listener;

    public RvItemModelSearchLoading(LoadNextPageListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void bind(SearchAdapter.SearchViewHolder holder) {
        listener.loadNextPage();
    }

}
