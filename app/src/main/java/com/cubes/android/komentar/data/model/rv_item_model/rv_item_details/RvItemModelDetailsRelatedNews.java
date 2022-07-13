package com.cubes.android.komentar.data.model.rv_item_model.rv_item_details;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemDetailsRelatedNewsBinding;
import com.cubes.android.komentar.ui.adapter.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.adapter.SearchAdapter;

import java.util.ArrayList;


public class RvItemModelDetailsRelatedNews implements ItemModelDetails {

    private ArrayList<News> list;

    public RvItemModelDetailsRelatedNews(ArrayList<News> list) {
        this.list = list;
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsRelatedNewsBinding binding = (RvItemDetailsRelatedNewsBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));

        SearchAdapter adapter = new SearchAdapter(list);

        binding.recyclerView.setAdapter(adapter);



    }
}
