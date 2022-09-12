package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

public class RvItemModelCategoryBig implements ItemModelCategory {

    private News news;
    private boolean isOnHomePage;
    private NewsListener listener;

    private int[] newsIdList;

    private boolean isMenuOpen = false;

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

        setMenuVisibility(isMenuOpen, binding);

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

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, newsIdList));

        binding.imageViewMenu.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
        });

        binding.imageViewClose.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
        });

        binding.imageViewComments.setOnClickListener(view -> listener.onNewsMenuCommentsClicked(news.id));

    }

    private void setMenuVisibility(boolean isMenuOpen, RvItemCategoryBigBinding binding) {
        if (isMenuOpen) {
            binding.imageViewMenu.setVisibility(View.GONE);
            binding.viewMenu.setVisibility(View.VISIBLE);
            binding.layoutRoot.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.grey_transparent));

        } else {
            binding.imageViewMenu.setVisibility(View.VISIBLE);
            binding.viewMenu.setVisibility(View.GONE);
            binding.layoutRoot.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        }
    }

    private void animateMenuVisibility(boolean isMenuOpen, RvItemCategoryBigBinding binding) {

        if (isMenuOpen) {
            binding.imageViewMenu.setVisibility(View.GONE);
            binding.viewMenu.setVisibility(View.VISIBLE);
            binding.layoutRoot.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.grey_transparent));
            YoYo.with(Techniques.SlideInRight).duration(300).playOn(binding.viewMenu);

        } else {
            binding.imageViewMenu.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideOutRight).duration(300).playOn(binding.viewMenu);
            binding.layoutRoot.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.white));

        }

    }

}
