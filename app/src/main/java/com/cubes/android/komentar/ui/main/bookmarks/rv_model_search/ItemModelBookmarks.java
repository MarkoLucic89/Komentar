package com.cubes.android.komentar.ui.main.bookmarks.rv_model_search;

import com.cubes.android.komentar.ui.main.bookmarks.BookmarksAdapter;

public interface ItemModelBookmarks {

    int getType();

    void bind(BookmarksAdapter.BookmarkViewHolder holder);

}
