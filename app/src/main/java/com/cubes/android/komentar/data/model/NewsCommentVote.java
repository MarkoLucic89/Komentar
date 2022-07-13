package com.cubes.android.komentar.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewsCommentVote {

    @PrimaryKey
    @NonNull
    public String comment;

    @ColumnInfo
    public boolean vote;

    public NewsCommentVote() {
    }

    public NewsCommentVote(String comment, boolean vote) {
        this.comment = comment;
        this.vote = vote;
    }
}
