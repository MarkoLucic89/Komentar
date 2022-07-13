package com.cubes.android.komentar.data.model.rv_item_model.rv_model_videos;

import android.content.Intent;
import android.net.Uri;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.adapter.VideosAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

public class RvItemModelVideos implements ItemModelVideo {

    private News news;

    public RvItemModelVideos(News news) {
        this.news = news;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(VideosAdapter.VideosViewHolder holder) {
        RvItemVideosBinding binding = (RvItemVideosBinding) holder.binding;

        Picasso.get().load(news.image).into(binding.imageViewPicture);
        binding.textViewTitle.setText(news.title);
        binding.textViewCategory.setText(news.category.name);
        binding.textViewTime.setText(MyMethodsClass.convertTime(news.created_at));

        holder.binding.getRoot().setOnClickListener(view -> MyMethodsClass.goToNewsDetailActivity(view, news.id));

//        holder.binding.getRoot().setOnClickListener(view -> {
//            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.url)));
//        });

    }


}
