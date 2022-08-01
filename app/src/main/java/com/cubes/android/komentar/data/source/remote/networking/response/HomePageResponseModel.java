package com.cubes.android.komentar.data.source.remote.networking.response;

import com.cubes.android.komentar.data.model.News;

import java.util.ArrayList;

public class HomePageResponseModel extends BaseResponseModel {

    public HomePageDataResponseModel data;

    public class HomePageDataResponseModel {

        public ArrayList<News> slider;
        public ArrayList<News> top;
        public ArrayList<News> editors_choice;
        public ArrayList<CategoryBoxResponseModel> category;
        public ArrayList<News> latest;
        public ArrayList<News> most_read;
        public ArrayList<News> most_comment;
        public ArrayList<News> videos;

    }

    public class CategoryBoxResponseModel {
        public int id;
        public String title;
        public String color;
        public ArrayList<News> news;
    }
}
