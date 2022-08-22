package com.cubes.android.komentar.data.source.local.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cubes.android.komentar.data.model.domain.NewsCommentVote;

import java.util.List;

@Dao
public interface NewsCommentsVoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsCommentVote vote);

    @Query("SELECT * FROM newscommentvote")
    List<NewsCommentVote> getNewsCommentVotes();

    @Query("SELECT * FROM newscommentvote WHERE comment LIKE :id")
    NewsCommentVote getNewsCommentVote(String id);
}
