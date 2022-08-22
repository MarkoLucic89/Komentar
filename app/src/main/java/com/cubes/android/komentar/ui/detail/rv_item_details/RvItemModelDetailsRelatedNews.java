package com.cubes.android.komentar.ui.detail.rv_item_details;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemDetailsRelatedNewsBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.detail.RelatedNewsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;


public class RvItemModelDetailsRelatedNews implements ItemModelDetails {

    private ArrayList<News> list;
    private NewsListener listener;

    public RvItemModelDetailsRelatedNews(NewsListener listener, ArrayList<News> list) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_details_related_news;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsRelatedNewsBinding binding = (RvItemDetailsRelatedNewsBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));

//        SearchAdapter adapter = new SearchAdapter(listener, list);
        RelatedNewsAdapter adapter = new RelatedNewsAdapter(list, listener, MyMethodsClass.initNewsIdList(list));

        binding.recyclerView.setAdapter(adapter);



    }
}
