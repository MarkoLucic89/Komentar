package com.cubes.android.komentar.ui.main.search.rv_model_search;


import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;


public class RvItemModelSearch implements ItemModelSearch {

    private News news;
    private NewsListener listener;

    private int[] newsIdList;

    private boolean isMenuOpen = false;


    public RvItemModelSearch(News news, NewsListener listener, int[] newsIdList) {
        this.news = news;
        this.listener = listener;
        this.newsIdList = newsIdList;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_category_small;
    }

    @Override
    public void bind(SearchAdapter.SearchViewHolder holder) {

        RvItemCategorySmallBinding binding = (RvItemCategorySmallBinding) holder.binding;

        setMenuVisibility(isMenuOpen, binding);

        Picasso.get().load(news.image).into(binding.imageView);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.category.name);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));

        binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, newsIdList));

        binding.imageViewMenu.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
        });

        binding.imageViewClose.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
        });

    }



    private void setMenuVisibility(boolean isMenuOpen, RvItemCategorySmallBinding binding) {
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

    private void animateMenuVisibility(boolean isMenuOpen, RvItemCategorySmallBinding binding) {

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
