package com.cubes.android.komentar.data.source.remote.networking.response.categories_response;

import com.cubes.android.komentar.data.model.Category;

import java.util.ArrayList;

public class CategoriesResponseModel {
    public int status;
    public String message;
    public ArrayList<Category> data;
}
