package com.cubes.android.komentar.ui.main.search.rv_model_search;


import android.graphics.Color;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RvItemModelSearch implements ItemModelSearch {

    private News news;
    private boolean isCategoryColored;
    private NewsListener listener;
    private ArrayList<News> newsList;

    public RvItemModelSearch(News news, NewsListener listener) {
        this.news = news;
        this.listener = listener;
    }

    public RvItemModelSearch(News news, boolean isCategoryColored, NewsListener listener) {
        this.news = news;
        this.isCategoryColored = isCategoryColored;
        this.listener = listener;
    }

    public RvItemModelSearch(News news, boolean b, NewsListener listener, ArrayList<News> newsList) {
        this.news = news;
        this.isCategoryColored = isCategoryColored;
        this.listener = listener;
        this.newsList = newsList;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(SearchAdapter.SearchViewHolder holder) {

        RvItemCategorySmallBinding binding = (RvItemCategorySmallBinding) holder.binding;

        Picasso.get().load(news.image).into(binding.imageView);
        binding.textViewTitle.setText(news.title);

        if (isCategoryColored) {
            binding.textViewCategory.setTextColor(Color.parseColor(news.category.color));
        }

        binding.textViewCategory.setText(news.category.name);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));

//        binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id));

        binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, news.url, newsList));


    }


}
