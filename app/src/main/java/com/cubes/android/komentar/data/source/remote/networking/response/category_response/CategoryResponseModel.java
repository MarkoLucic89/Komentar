package com.cubes.android.komentar.data.source.remote.networking.response.category_response;

import com.cubes.android.komentar.data.model.News;

import java.util.ArrayList;

public class CategoryResponseModel {
    public int status;
    public String message;
    public CategoryDataResponseModel data;

    public class CategoryDataResponseModel {
        public CategoryDataPaginationResponseModel pagination;
        public ArrayList<News> news;
    }

    public class CategoryDataPaginationResponseModel {
        public int total;
        public int count;
        public int current_page;
        public boolean has_more_pages;
        public int last_page;
        public int per_page;
    }

}
