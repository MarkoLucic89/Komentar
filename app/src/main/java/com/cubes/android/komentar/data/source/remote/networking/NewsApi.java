package com.cubes.android.komentar.data.source.remote.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApi {

    private static NewsApi instance = null;
    public static final String BASE_URL = "https://komentar.rs/wp-json/";

    private NewsService newsService;

    public NewsService getNewsService() {
        return this.newsService;
    }

    public static NewsApi getInstance() {
        if (instance == null) {
            instance = new NewsApi();
        }

        return instance;
    }

    private NewsApi() {
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
