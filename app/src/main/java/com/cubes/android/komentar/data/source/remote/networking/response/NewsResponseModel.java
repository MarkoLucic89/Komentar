package com.cubes.android.komentar.data.source.remote.networking.response;

import com.cubes.android.komentar.data.model.NewsApi;

import java.util.ArrayList;

public class NewsResponseModel extends BaseResponseModel {
    public NewsDataResponseModel data;

    public class NewsDataResponseModel {

        public NewsPaginationResponseModel pagination;
        public ArrayList<NewsApi> news;

    }

    public class NewsPaginationResponseModel {
        public int total;
        public int count;
        public int current_page;
        public boolean has_more_pages;
        public int last_page;
        public int per_page;
    }


}
