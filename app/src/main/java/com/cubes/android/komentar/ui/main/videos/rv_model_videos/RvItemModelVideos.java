package com.cubes.android.komentar.ui.main.videos.rv_model_videos;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

public class RvItemModelVideos implements ItemModelVideo {

    private News news;
    private NewsListener listener;

    private int[] newsIdList;

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

        Picasso.get().load(news.image).into(binding.imageViewPicture);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.category.name);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.createdAt));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, news.url, newsIdList));

    }


}
