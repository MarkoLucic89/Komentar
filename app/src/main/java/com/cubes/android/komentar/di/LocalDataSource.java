package com.cubes.android.komentar.di;

import com.cubes.android.komentar.data.source.local.database.dao.NewsCommentsVoteDao;

public class LocalDataSource {

    public NewsCommentsVoteDao voteDao;

    public LocalDataSource(NewsCommentsVoteDao voteDao) {
        this.voteDao = voteDao;
    }
}
