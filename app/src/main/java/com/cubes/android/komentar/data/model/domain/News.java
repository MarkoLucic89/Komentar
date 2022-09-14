package com.cubes.android.komentar.data.model.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class News implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "category_name")
    public String categoryName;

    @ColumnInfo(name = "category_color")
    public String categoryColor;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "url")
    public String url;

    @Ignore
    public boolean isInBookmarks;
}
