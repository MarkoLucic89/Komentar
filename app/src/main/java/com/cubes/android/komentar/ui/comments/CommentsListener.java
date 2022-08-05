package com.cubes.android.komentar.ui.comments;

import android.content.Context;
import android.view.View;

public interface CommentsListener {

    void onLikeListener(int id, boolean vote);

    void onDislikeListener(int id, boolean vote);

    void goOnPostCommentActivity(Context context, int news, int reply_id);

}
