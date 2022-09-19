package com.cubes.android.komentar.ui.main.bookmarks.rv_model_bookmark;


import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.bookmarks.BookmarksAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;


public class RvItemModelBookmarks implements ItemModelBookmarks {

    private News news;
    private NewsListener listener;

    private int[] newsIdList;

    private boolean isMenuOpen = false;

    private RvItemCategorySmallBinding binding;


    public RvItemModelBookmarks(News news, NewsListener listener, int[] newsIdList) {
        this.news = news;
        this.listener = listener;
        this.newsIdList = newsIdList;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_category_small;
    }

    @Override
    public void bind(BookmarksAdapter.BookmarkViewHolder holder) {

        binding = (RvItemCategorySmallBinding) holder.binding;

        setMenuVisibility(isMenuOpen, binding);

        binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark);

        Picasso.get().load(news.image).into(binding.imageView);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.categoryName);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));

        binding.getRoot().setOnClickListener(view -> {

            closeMenu();

            listener.onNewsClicked(news.id, newsIdList);
        });

        binding.imageViewMenu.setOnClickListener(view -> {

            listener.closeOtherMenus();

            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
        });

        binding.viewMenu.setOnClickListener(view -> {

        });

        binding.imageViewClose.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
        });

        binding.imageViewComments.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
            listener.onNewsMenuCommentsClicked(news.id);
        });

        binding.imageViewShare.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
            listener.onNewsMenuShareClicked(news.url);
        });

        binding.imageViewFavorites.setOnClickListener(view -> {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
            listener.onNewsMenuFavoritesClicked(news);
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

    @Override
    public void closeMenu() {

        if (isMenuOpen) {
            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
        }

    }
}
