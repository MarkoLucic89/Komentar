package com.cubes.android.komentar.data.source.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cubes.android.komentar.data.model.domain.News;

import java.util.List;

@Dao
public interface NewsBookmarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    @Query("SELECT * FROM news")
    List<News> getBookmarkNews();

    @Delete()
    void delete(News news);
}