package com.cubes.android.komentar.data.model.domain;

import java.util.ArrayList;

public class NewsDetails {

    public int id;
    public String title;
    public String url;
    public ArrayList<NewsTag> tags;
    public ArrayList<NewsComment> commentsTop;
    public boolean commentEnabled;
    public int commentsCount;
    public ArrayList<News> relatedNews;

}
