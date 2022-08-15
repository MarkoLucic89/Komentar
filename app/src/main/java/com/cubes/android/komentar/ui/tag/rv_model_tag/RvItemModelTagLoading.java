package com.cubes.android.komentar.ui.tag.rv_model_tag;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tag.TagAdapter;

public class RvItemModelTagLoading implements ItemModelTag{

    private NewsListener listener;

    public RvItemModelTagLoading(NewsListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_loading;
    }

    @Override
    public void bind(TagAdapter.TagViewHolder holder) {
        listener.loadNextPage();
    }

}
