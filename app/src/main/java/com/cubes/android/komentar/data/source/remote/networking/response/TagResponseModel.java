package com.cubes.android.komentar.data.source.remote.networking.response;

import com.cubes.android.komentar.data.model.NewsApi;

import java.util.ArrayList;

public class TagResponseModel extends BaseResponseModel{

    public TagDataResponseModel data;

    public class TagDataResponseModel{
        public TagDataPaginationResponseModel pagination;
        public ArrayList<NewsApi> news;

    }

    public class TagDataPaginationResponseModel {
        public int total;
        public int count;
        public int current_page;
        public boolean has_more_pages;
        public int last_page;
        public int per_page;
    }
}
