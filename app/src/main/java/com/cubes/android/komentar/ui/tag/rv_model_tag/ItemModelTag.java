package com.cubes.android.komentar.ui.tag.rv_model_tag;

import com.cubes.android.komentar.ui.tag.TagAdapter;

public interface ItemModelTag {

    int getType();

    void bind(TagAdapter.TagViewHolder holder);

    default void closeMenu() {

    }

}
