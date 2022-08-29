package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

public class RvItemModelCategoryBig implements ItemModelCategory {

    private News news;
    private boolean isOnHomePage;
    private NewsListener listener;

    private int[] newsIdList;

    public RvItemModelCategoryBig(News news, boolean isOnHomePage, NewsListener listener, int[] newsIdList) {
        this.news = news;
        this.isOnHomePage = isOnHomePage;
        this.listener = listener;
        this.newsIdList = newsIdList;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_category_big;
    }

    @Override
    public void bind(CategoryAdapter.CategoryViewHolder holder) {

        RvItemCategoryBigBinding binding = (RvItemCategoryBigBinding) holder.binding;

        if (isOnHomePage) {
            binding.textViewCategory.setVisibility(View.GONE);
            binding.viewLine.setVisibility(View.GONE);
        } else {
            binding.textViewCategory.setVisibility(View.VISIBLE);
            binding.viewLine.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(news.image).into(binding.imageView);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.category.name);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id,  newsIdList));

    }

}
