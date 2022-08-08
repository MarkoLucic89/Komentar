package com.cubes.android.komentar.ui.detail.rv_item_details;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cubes.android.komentar.data.model.NewsTag;
import com.cubes.android.komentar.databinding.RvItemDetailsTagsBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsTagsAdapter;

import java.util.ArrayList;

public class RvItemModelDetailsTags implements ItemModelDetails {

    private ArrayList<NewsTag> tags;
    private NewsDetailsTagsAdapter.TagListener tagListener;

    public RvItemModelDetailsTags(NewsDetailsTagsAdapter.TagListener tagListener, ArrayList<NewsTag> tags) {
        this.tags = tags;
        this.tagListener = tagListener;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsTagsBinding binding = (RvItemDetailsTagsBinding) holder.binding;

//        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
//                3,
//                StaggeredGridLayoutManager.VERTICAL
//        ));

        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                getSpanCount(),
                StaggeredGridLayoutManager.HORIZONTAL
        ));

        binding.recyclerView.setAdapter(new NewsDetailsTagsAdapter(tagListener, tags));
    }

    private int getSpanCount() {

        if (tags.size() < 4) {
            return 1;
        } else if (tags.size() < 7) {
            return 2;
        } else if (tags.size() < 13) {
            return 3;
        } else {
            return 4;
        }

    }
}
