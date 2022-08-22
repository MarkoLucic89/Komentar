package com.cubes.android.komentar.ui.detail.rv_item_details;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.NewsTag;
import com.cubes.android.komentar.databinding.RvItemDetailsTagsBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsTagsAdapter;
import com.google.android.flexbox.FlexboxLayoutManager;

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
        return R.layout.rv_item_details_tags;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsTagsBinding binding = (RvItemDetailsTagsBinding) holder.binding;

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(binding.getRoot().getContext());
        binding.recyclerView.setLayoutManager(flexboxLayoutManager);

        binding.recyclerView.setAdapter(new NewsDetailsTagsAdapter(tagListener, tags));
    }

}
