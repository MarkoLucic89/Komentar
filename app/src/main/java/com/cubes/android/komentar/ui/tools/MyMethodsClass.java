package com.cubes.android.komentar.ui.tools;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.cubes.android.komentar.data.model.domain.HomePageData;
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


    public static int[] initNewsIdListFromHomePage(HomePageData data) {

        ArrayList<News> newsList = new ArrayList<>();

        addNewsToList(newsList, data.slider);
        addNewsToList(newsList, data.top);
        addNewsToList(newsList, data.latest);
        addNewsToList(newsList, data.mostRead);
        addNewsToList(newsList, data.mostCommented);

        for (int i = 0; i < data.category.size(); i++) {
            if (data.category.get(i).title.equalsIgnoreCase("SPORT")) {
                for (int j = 0; j < 5; j++) {
                    newsList.add(data.category.get(i).news.get(j));
                }
                break;
            }
        }

//        for (HomePageData.CategoryBox categoryBox : data.category) {
//            if (categoryBox.title.equalsIgnoreCase("SPORT")) {
//                addNewsToList(newsList, categoryBox.news);
//            }
//        }
        addNewsToList(newsList, data.editorsChoice);
        addNewsToList(newsList, data.videos);
//        for (HomePageData.CategoryBox categoryBox : data.category) {
//            addNewsToList(newsList, categoryBox.news);
//        }
        for (int i = 0; i < data.category.size(); i++) {
            if (!data.category.get(i).title.equalsIgnoreCase("SPORT")) {
                for (int j = 0; j < 5; j++) {
                    newsList.add(data.category.get(i).news.get(j));
                }
            }
        }
        return MyMethodsClass.initNewsIdList(newsList);
    }

    private static void addNewsToList(ArrayList<News> newsList, ArrayList<News> newsListApi) {
        for (News news : newsListApi) {
            newsList.add(news);
        }
    }

    public static void startProgressBarAnimation(CardView progressBar) {

        progressBar.animate().scaleX(2f).scaleY(2f).alpha(0f).setDuration(1000).withEndAction(() -> {
            progressBar.setScaleX(1f);
            progressBar.setScaleY(1f);
            progressBar.setAlpha(1f);
        });

    }
}
