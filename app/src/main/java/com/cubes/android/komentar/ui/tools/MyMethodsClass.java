package com.cubes.android.komentar.ui.tools;

import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


import com.cubes.android.komentar.ui.category.CategoryActivity;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.detail.NewsDetailsActivity;
import com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager.DetailsActivity;
import com.cubes.android.komentar.ui.post_comment.PostCommentActivity;
import com.cubes.android.komentar.ui.tag.TagActivity;

public class MyMethodsClass {

    public static String convertTime(String created_at) {
        return created_at.substring(11, 16);
    }

    public static String convertDateTime(String created_at) {
        return created_at.substring(0, (created_at.length() - 3));
    }

    public static void goToNewsDetailActivity(View view, int id) {
        Intent intent = new Intent(view.getContext(), NewsDetailsActivity.class);
        intent.putExtra("news_id", id);
        view.getContext().startActivity(intent);
    }

    public static void goToTagActivity(View view, int id, String title) {
        Intent intent = new Intent(view.getContext(), TagActivity.class);
        intent.putExtra("tag_id", id);
        intent.putExtra("tag_title", title);
        view.getContext().startActivity(intent);
    }

    public static void goToCommentsActivity(View view, int id) {
        Intent intent = new Intent(view.getContext(), CommentsActivity.class);
        intent.putExtra("news_id", id);
        view.getContext().startActivity(intent);
    }

    public static void goToPostCommentsActivity(View view, String parentId) {
        Intent intent = new Intent(view.getContext(), PostCommentActivity.class);
        intent.putExtra("parent_id", parentId);
        view.getContext().startActivity(intent);
    }

    public static void goToPostCommentsActivity(Context context, int news, int reply_id) {
        Intent intent = new Intent(context, PostCommentActivity.class);
        intent.putExtra("news", news);
        intent.putExtra("reply_id", reply_id);
        context.startActivity(intent);
    }

    public static void goToSubcategoriesActivity(View view, int categoryId, int subcategoryID) {
        Intent intent = new Intent(view.getContext(), CategoryActivity.class);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("subcategory_id", subcategoryID);
        view.getContext().startActivity(intent);
    }

    public static void startRefreshAnimation(ImageView imageView) {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        imageView.startAnimation(rotate);

    }
}
