package com.cubes.android.komentar.data.model.domain;

import java.util.ArrayList;

public class NewsDetails {

    public int id;
    public String url;
    public ArrayList<NewsTag> tags;
    public ArrayList<NewsComment> comments_top_n;
    public boolean comment_enabled;
    public int comments_count;
    public ArrayList<News> related_news;

}
