package com.cubes.android.komentar.ui.main.bookmarks.rv_model_bookmark;

import com.cubes.android.komentar.ui.main.bookmarks.BookmarksAdapter;

public interface ItemModelBookmarks {

    int getType();

    void bind(BookmarksAdapter.BookmarkViewHolder holder);

    default void closeMenu() {

    }

}
