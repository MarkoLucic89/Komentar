package com.cubes.android.komentar.data.model;

import java.util.ArrayList;

public class NewsComment {

    public int negative_votes;
    public int positive_votes;
    public String created_at;
    public String news;
    public String name;
    public String parent_comment;
    public String id;
    public String content;
    public ArrayList<NewsComment> children;

}
