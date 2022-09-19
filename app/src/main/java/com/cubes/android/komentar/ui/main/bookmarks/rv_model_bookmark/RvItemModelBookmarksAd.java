package com.cubes.android.komentar.ui.main.bookmarks.rv_model_bookmark;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.ui.main.bookmarks.BookmarksAdapter;

public class RvItemModelBookmarksAd implements ItemModelBookmarks {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(BookmarksAdapter.BookmarkViewHolder holder) {

//        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

    }
}
