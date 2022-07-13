package com.cubes.android.komentar.data.model.rv_item_model.rv_model_search;

import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.ui.adapter.CategoryAdapter;
import com.cubes.android.komentar.ui.adapter.SearchAdapter;

public interface ItemModelSearch {


    int getType();

    void bind(SearchAdapter.SearchViewHolder holder);

}
