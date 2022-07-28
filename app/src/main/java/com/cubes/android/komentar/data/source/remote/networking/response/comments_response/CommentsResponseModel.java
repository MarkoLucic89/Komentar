package com.cubes.android.komentar.data.source.remote.networking.response.comments_response;

import com.cubes.android.komentar.data.model.NewsComment;


import java.util.ArrayList;

public class CommentsResponseModel {
    public int status;
    public String message;
    public ArrayList<NewsComment> data;
}
