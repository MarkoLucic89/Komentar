package com.cubes.android.komentar.data.source.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.data.source.local.database.dao.NewsCommentsVoteDao;

@Database(entities = NewsCommentVote.class, version = 1)
public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase instance;

    public abstract NewsCommentsVoteDao voteDao();

    public static NewsDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(
                    context,
                    NewsDatabase.class,
                    "database-komentar.rs"
            )
                    .allowMainThreadQueries()
                    .build();

        }

        return instance;
    }
}
