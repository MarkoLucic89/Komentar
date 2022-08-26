package com.cubes.android.komentar.ui.tools;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.cubes.android.komentar.data.model.domain.News;

import java.util.ArrayList;

public class MyMethodsClass {

    public static int[] initNewsIdList(ArrayList<News> newsList) {

        int[] newsIdList = new int[newsList.size()];

        for (int i = 0; i < newsList.size(); i++) {
            newsIdList[i] = newsList.get(i).id;
        }

        return newsIdList;
    }

    public static String convertTime(String created_at) {
        return created_at.substring(11, 16);
    }

    public static String convertDateTime(String created_at) {
        return created_at.substring(0, (created_at.length() - 3));
    }

    public static void startRefreshAnimation(ImageView imageView) {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        imageView.startAnimation(rotate);

    }


}
