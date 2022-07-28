package com.cubes.android.komentar.ui.main.home.rv_item_home;

import android.graphics.Color;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.home.HomeAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

public class RvItemModelHomeSmallNews implements ItemModelHome {

    private News news;

    public RvItemModelHomeSmallNews(News news) {
        this.news = news;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void bind(HomeAdapter.HomeViewHolder holder) {

        RvItemCategorySmallBinding binding = (RvItemCategorySmallBinding) holder.binding;

        Picasso.get().load(news.image).into(binding.imageView);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.category.name);
        binding.textViewCategory.setTextColor(Color.parseColor(news.category.color));
        binding.textViewTime.setText(getTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> MyMethodsClass.goToNewsDetailActivity(view, news.id));

    }

    private String getTime(String created_at) {
        return created_at.substring(11, 16);
    }
}

