package com.cubes.android.komentar.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewsCommentVote {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "comment")
    public String id;

    @ColumnInfo
    public boolean vote;

    public NewsCommentVote() {
    }

    public NewsCommentVote(String id, boolean vote) {
        this.id = id;
        this.vote = vote;
    }
}
