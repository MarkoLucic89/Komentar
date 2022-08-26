package com.cubes.android.komentar.data.source.remote.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRetrofit {

    public static final String BASE_URL = "https://komentar.rs/wp-json/";

    private NewsService newsService;

    public NewsRetrofit() {
        buildRetrofit(BASE_URL);
    }

    public NewsService getNewsService() {
        return this.newsService;
    }

    private void buildRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsService = retrofit.create(NewsService.class);
    }

}
