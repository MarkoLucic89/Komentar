package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import android.graphics.Color;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.squareup.picasso.Picasso;

public class RvItemModelHomeSmallNews implements ItemModelHome {

    private News news;
    private NewsListener listener;

    private int[] newsIdList;

    public RvItemModelHomeSmallNews(News news, NewsListener listener, int[] newsIdList) {

        this.news = news;
        this.listener = listener;
        this.newsIdList = newsIdList;

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
        binding.textViewTime.setText(getTime(news.createdAt));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, newsIdList));

    }

    private String getTime(String created_at) {
        return created_at.substring(11, 16);
    }
}

