package com.cubes.android.komentar.ui.detail.rv_item_details;

import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;

public interface ItemModelDetails {

    int getType();

    void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder);
}
