package com.cubes.android.komentar.ui.comments.rv_item_comments;

import com.cubes.android.komentar.ui.comments.CommentsAdapter;

public interface ItemModelComments {

    int getCommentsId();

    int getType();

    void bind(CommentsAdapter.CommentsViewHolder holder);
}
