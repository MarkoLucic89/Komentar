package com.cubes.android.komentar.data.model.response.news_details_response;

import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.NewsTag;

import java.util.ArrayList;

public class NewsDetailsDataResponseModel {
    public int id;
    public String image;
    public String image_source;
    public String author_name;
    public String source;
    public Category category;
    public String title;
    public String description;
    public int comment_enabled;
    public int comments_count;
    public int shares_count;
    public String created_at;
    public String url;
    public String click_type;
    public ArrayList<NewsTag> tags;
    public ArrayList<News> related_news;
    public ArrayList<News> category_news;
    public ArrayList<NewsComment> comments_top_n;

}
