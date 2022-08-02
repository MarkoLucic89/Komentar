package com.cubes.android.komentar.ui.comments;

public interface CommentsListener {

    void onLikeListener(int id, boolean vote);

    void onDislikeListener(int id, boolean vote);

}
