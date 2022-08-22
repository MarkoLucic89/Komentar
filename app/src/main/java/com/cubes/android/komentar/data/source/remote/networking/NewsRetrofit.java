package com.cubes.android.komentar.data.source.remote.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRetrofit {

    private static NewsRetrofit instance = null;
    public static final String BASE_URL = "https://komentar.rs/wp-json/";

    private NewsService newsService;

    public NewsService getNewsService() {
        return this.newsService;
    }

    public static NewsRetrofit getInstance() {
        if (instance == null) {
            instance = new NewsRetrofit();
        }

        return instance;
    }

    private NewsRetrofit() {
        buildRetrofit(BASE_URL);
    }

    private void buildRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsService = retrofit.create(NewsService.class);
    }

}
