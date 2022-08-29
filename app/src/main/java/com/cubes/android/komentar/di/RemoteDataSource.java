package com.cubes.android.komentar.di;

import com.cubes.android.komentar.data.source.remote.networking.NewsService;

public class RemoteDataSource {

    public final NewsService newsService;

    public RemoteDataSource(NewsService newsService) {
        this.newsService = newsService;
    }


}
