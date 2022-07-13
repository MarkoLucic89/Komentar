package com.cubes.android.komentar.data.model.rv_item_model.rv_model_category;

import android.view.View;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.ui.adapter.CategoryAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

public class RvItemModelCategoryBig implements ItemModelCategory {

    private News news;
    private boolean isOnHomePage;

    public RvItemModelCategoryBig(News news) {
        this.news = news;
        isOnHomePage = false;
    }

    public RvItemModelCategoryBig(News news, boolean isOnHomePage) {
        this.news = news;
        this.isOnHomePage = isOnHomePage;
    }

    @Override
    public int getType() {
        return 0;
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
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> MyMethodsClass.goToNewsDetailActivity(view, news.id));

    }

}
