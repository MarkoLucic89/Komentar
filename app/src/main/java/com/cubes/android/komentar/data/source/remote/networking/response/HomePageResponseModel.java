package com.cubes.android.komentar.data.source.remote.networking.response;

import com.cubes.android.komentar.data.model.NewsApi;
import com.cubes.android.komentar.data.model.domain.News;

import java.util.ArrayList;

public class HomePageResponseModel extends BaseResponseModel {

    public HomePageDataResponseModel data;

    public class HomePageDataResponseModel {

        public ArrayList<NewsApi> slider;
        public ArrayList<NewsApi> top;
        public ArrayList<NewsApi> editors_choice;
        public ArrayList<CategoryBoxResponseModel> category;
        public ArrayList<NewsApi> latest;
        public ArrayList<NewsApi> most_read;
        public ArrayList<NewsApi> most_comented;
        public ArrayList<NewsApi> videos;

    }

    public class CategoryBoxResponseModel {
        public int id;
        public String title;
        public String color;
        public ArrayList<NewsApi> news;
    }
}
