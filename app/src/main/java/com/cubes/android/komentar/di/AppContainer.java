package com.cubes.android.komentar.di;

import android.content.Context;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.local.database.NewsDatabase;
import com.cubes.android.komentar.data.source.remote.networking.NewsRetrofit;

public class AppContainer {

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    public NewsDatabase room;
    public DataRepository dataRepository;

    public AppContainer(Context context) {

        NewsRetrofit retrofit = new NewsRetrofit();

        room = NewsDatabase.getInstance(context);

        localDataSource = new LocalDataSource(room.voteDao());
        remoteDataSource = new RemoteDataSource(retrofit.getNewsService());

        dataRepository = new DataRepository(localDataSource, remoteDataSource);

    }
}
