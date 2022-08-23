package com.cubes.android.komentar.data.source.remote.networking.response;

import com.cubes.android.komentar.data.model.CategoryApi;
import com.cubes.android.komentar.data.model.NewsApi;
import com.cubes.android.komentar.data.model.NewsCommentApi;
import com.cubes.android.komentar.data.model.NewsTagApi;

import java.util.ArrayList;

public class NewsDetailsResponseModel extends BaseResponseModel {

    public NewsDetailsDataResponseModel data;

    public class NewsDetailsDataResponseModel {
        public int id;
        public String image;
        public String image_source;
        public String author_name;
        public String source;
        public CategoryApi category;
        public String title;
        public String description;
        public int comment_enabled;
        public int comments_count;
        public int shares_count;
        public String created_at;
        public String url;
        public String click_type;
        public ArrayList<NewsTagApi> tags;
        public ArrayList<NewsApi> related_news;
        public ArrayList<NewsApi> category_news;
        public ArrayList<NewsCommentApi> comments_top_n;

    }
}
