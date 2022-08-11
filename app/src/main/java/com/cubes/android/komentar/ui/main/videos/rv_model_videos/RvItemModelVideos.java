package com.cubes.android.komentar.ui.main.videos.rv_model_videos;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RvItemModelVideos implements ItemModelVideo {

    private News news;
    private NewsListener listener;

    private ArrayList<News> newsList = new ArrayList<>();

    public RvItemModelVideos(NewsListener listener, News news) {
        this.news = news;
        this.listener = listener;

    }

    public RvItemModelVideos(News news, NewsListener listener, ArrayList<News> newsList) {
        this.news = news;
        this.listener = listener;
        this.newsList = newsList;
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
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> listener.onNewsClicked(news.id, news.url, newsList));

    }


}
