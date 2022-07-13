package com.cubes.android.komentar.data.model.rv_item_model.rv_item_details;

import com.cubes.android.komentar.ui.adapter.NewsDetailsAdapter;

public interface ItemModelDetails {

    int getType();

    void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder);
}
