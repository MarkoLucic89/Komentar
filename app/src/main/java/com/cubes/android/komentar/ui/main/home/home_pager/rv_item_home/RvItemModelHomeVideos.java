package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

public class RvItemModelHomeVideos implements ItemModelHome {

    private News news;
    private NewsListener listener;

    private boolean isMenuOpen = false;

    private RvItemVideosBinding binding;

    public RvItemModelHomeVideos(News news, NewsListener listener) {
        this.news = news;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_videos;
    }

    @Override
    public void bind(HomePagerAdapter.HomeViewHolder holder) {

        binding = (RvItemVideosBinding) holder.binding;


        if (news.isInBookmarks) {
            binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark);
        } else {
            binding.imageViewFavorites.setImageResource(R.drawable.ic_bookmark_border);
        }

        setMenuVisibility(isMenuOpen, binding);

        Picasso.get().load(news.image).into(binding.imageViewPicture);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.categoryName);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));

        binding.getRoot().setOnClickListener(view -> {

            listener.closeOtherMenus();

            listener.onNewsClicked(news.id);
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

    private void setMenuVisibility(boolean isMenuOpen, RvItemVideosBinding binding) {
        if (isMenuOpen) {
            binding.imageViewMenu.setVisibility(View.GONE);
            binding.viewMenu.setVisibility(View.VISIBLE);

        } else {
            binding.imageViewMenu.setVisibility(View.VISIBLE);
            binding.viewMenu.setVisibility(View.GONE);
        }
    }

    private void animateMenuVisibility(boolean isMenuOpen, RvItemVideosBinding binding) {

        if (isMenuOpen) {
            binding.imageViewMenu.setVisibility(View.GONE);
            binding.viewMenu.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideInRight).duration(300).playOn(binding.viewMenu);

        } else {
            binding.imageViewMenu.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideOutRight).duration(300).playOn(binding.viewMenu);

        }

    }

    @Override
    public void closeMenu() {

        if (isMenuOpen) {
            isMenuOpen = false;
            animateMenuVisibility(false, binding);
        }

    }

}
