package com.cubes.android.komentar.data.source.remote.networking.response.tag_response;

import com.cubes.android.komentar.data.model.News;

import java.util.ArrayList;

public class TagDataResponseModel {
    public TagDataPaginationResponseModel pagination;
    public ArrayList<News> news;
}
