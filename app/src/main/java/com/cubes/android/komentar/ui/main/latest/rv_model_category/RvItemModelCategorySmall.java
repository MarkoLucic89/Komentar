package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import android.view.View;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

public class RvItemModelCategorySmall implements ItemModelCategory {

    private News news;
    private boolean isOnHomePage;
    private NewsListener listener;

    public RvItemModelCategorySmall(News news, boolean isOnHomePage, NewsListener listener) {
        this.news = news;
        this.isOnHomePage = isOnHomePage;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void bind(CategoryAdapter.CategoryViewHolder holder) {

        RvItemCategorySmallBinding binding = (RvItemCategorySmallBinding) holder.binding;

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
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id));

    }

}
