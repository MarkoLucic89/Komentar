package com.cubes.android.komentar.ui.main.videos.rv_model_videos;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

public class RvItemModelVideos implements ItemModelVideo {

    private News news;
    private NewsListener listener;

    private int[] newsIdList;

    private boolean isMenuOpen = false;

    public RvItemModelVideos(News news, NewsListener listener, int[] newsIdList) {
        this.news = news;
        this.listener = listener;
        this.newsIdList = newsIdList;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_videos;
    }

    @Override
    public void bind(VideosAdapter.VideosViewHolder holder) {

        RvItemVideosBinding binding = (RvItemVideosBinding) holder.binding;

        setMenuVisibility(isMenuOpen, binding);

        Picasso.get().load(news.image).into(binding.imageViewPicture);
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


}
