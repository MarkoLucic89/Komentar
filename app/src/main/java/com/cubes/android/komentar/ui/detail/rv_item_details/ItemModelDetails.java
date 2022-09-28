package com.cubes.android.komentar.ui.detail.rv_item_details;

import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;

public interface ItemModelDetails {

    int getType();

    void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder);

    default int getCommentsId() {
        return -1;
    }

    default void updateLikeUi() {

    }

    default void updateDislikeUi() {

    }

    default void closeMenu() {

    }

    default News getNews() {
        return null;
    }

    default void updateBookmarkUi(boolean isSaved) {

    }
}
