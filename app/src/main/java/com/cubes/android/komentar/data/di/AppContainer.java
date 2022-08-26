package com.cubes.android.komentar.data.di;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.NewsRetrofit;

public class AppContainer {

    NewsRetrofit api = new NewsRetrofit();

    public DataRepository dataRepository = new DataRepository(api);

}
