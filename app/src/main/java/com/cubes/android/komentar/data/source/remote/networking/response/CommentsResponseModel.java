package com.cubes.android.komentar.data.source.remote.networking.response;

import com.cubes.android.komentar.data.model.NewsCommentApi;
import com.cubes.android.komentar.data.model.domain.NewsComment;


import java.util.ArrayList;

public class CommentsResponseModel extends BaseResponseModel{
    public ArrayList<NewsCommentApi> data;
}
