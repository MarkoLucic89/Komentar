package com.cubes.android.komentar.ui.main.search.rv_model_search;

import com.cubes.android.komentar.ui.main.search.SearchAdapter;

public interface ItemModelSearch {

    int getType();

    void bind(SearchAdapter.SearchViewHolder holder);

}
