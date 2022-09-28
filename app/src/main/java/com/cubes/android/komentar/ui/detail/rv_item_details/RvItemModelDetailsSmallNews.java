package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

public class RvItemModelDetailsSmallNews implements ItemModelDetails {

    private News news;
    private boolean isOnHomePage;
    private NewsListener listener;

    private boolean isMenuOpen = false;

    private RvItemCategorySmallBinding binding;

    private int[] newsIdList;


    public RvItemModelDetailsSmallNews(News news, boolean isOnHomePage, NewsListener listener,int[] newsIdList) {
        this.news = news;
        this.isOnHomePage = isOnHomePage;
        this.listener = listener;
        this.newsIdList = newsIdList;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_category_small;
    }

    @Override
    public News getNews() {
        return news;
    }

    @Override
    public void updateBookmarkUi(boolean isSaved) {

        if (isSaved) {
            binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark);
        } else {
            binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark_border);
        }

    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        binding = (RvItemCategorySmallBinding) holder.binding;

        if (news.isInBookmarks) {
            binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark);
        } else {
            binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark_border);
        }

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
        binding.textViewCategory.setText(news.categoryName);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));

        holder.binding.getRoot().setOnClickListener(view -> {

            listener.closeOtherMenus();

            listener.onNewsClicked(news.id, newsIdList);

        });

        binding.viewMenu.setOnClickListener(view -> {
        });

        binding.imageViewMenu.setOnClickListener(view -> {

            listener.closeOtherMenus();

            isMenuOpen = !isMenuOpen;
            animateMenuVisibility(isMenuOpen, binding);
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

            if (!news.isInBookmarks) {
                binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark);
            } else {
                binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark_border);
            }

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
