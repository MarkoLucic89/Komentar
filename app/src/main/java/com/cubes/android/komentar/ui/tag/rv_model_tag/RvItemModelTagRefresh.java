package com.cubes.android.komentar.ui.tag.rv_model_tag;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tag.TagAdapter;

public class RvItemModelTagRefresh implements ItemModelTag {

    private NewsListener listener;

    public RvItemModelTagRefresh(NewsListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_refresh;
    }

    @Override
    public void bind(TagAdapter.TagViewHolder holder) {
        RvItemRefreshBinding binding = (RvItemRefreshBinding) holder.binding;

        binding.getRoot().setOnClickListener(view -> {
            listener.loadNextPage();
        });
    }

}
