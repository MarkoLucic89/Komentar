package com.cubes.android.komentar.data.source.local.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cubes.android.komentar.data.model.NewsCommentVote;

@Dao
public interface NewsCommentsVoteDao {

    @Insert
    void insert(NewsCommentVote vote);

    @Query("SELECT * FROM newscommentvote WHERE comment LIKE :id")
    NewsCommentVote getNewsCommentVote(String id);
}
