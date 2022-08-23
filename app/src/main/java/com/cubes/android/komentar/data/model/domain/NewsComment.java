package com.cubes.android.komentar.data.model.domain;

import java.util.ArrayList;

public class NewsComment {

    public int negativeVotes;
    public int positiveVotes;
    public String createdAt;
    public String newsId;
    public String name;
    public String parentCommentId;
    public String id;
    public String content;
    public ArrayList<NewsComment> children;

    public NewsCommentVote newsCommentVote;

}
