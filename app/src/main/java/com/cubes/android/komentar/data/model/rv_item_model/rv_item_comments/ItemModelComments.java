package com.cubes.android.komentar.data.model.rv_item_model.rv_item_comments;

import com.cubes.android.komentar.ui.adapter.CommentsAdapter;

public interface ItemModelComments {

    int getType();

    void bind(CommentsAdapter.CommentsViewHolder holder);
}
