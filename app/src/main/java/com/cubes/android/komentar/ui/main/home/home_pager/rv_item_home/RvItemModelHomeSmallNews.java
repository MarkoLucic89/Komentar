package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import android.graphics.Color;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RvItemModelHomeSmallNews implements ItemModelHome {

    private News news;
    private NewsListener listener;

    private int[] newsIdList;

    public RvItemModelHomeSmallNews(News news, NewsListener listener, ArrayList<News> newsList) {
        this.news = news;
        this.listener = listener;

        this.newsIdList = MyMethodsClass.initNewsIdList(newsList);

    }

    @Override
    public int getType() {
        return R.layout.rv_item_category_small;
    }

    @Override
    public void bind(HomePagerAdapter.HomeViewHolder holder) {

        RvItemCategorySmallBinding binding = (RvItemCategorySmallBinding) holder.binding;

        Picasso.get().load(news.image).into(binding.imageView);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.category.name);
        binding.textViewCategory.setTextColor(Color.parseColor(news.category.color));
        binding.textViewTime.setText(getTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, news.url, newsIdList));

    }

    private String getTime(String created_at) {
        return created_at.substring(11, 16);
    }
}

