package com.cubes.android.komentar.data.model;


import java.util.ArrayList;

public class CategoryApi {
    public String type;
    public int id;
    public String name;
    public String color;
    public String main_image;
    public String description;
    public CategoryApi subcategory;
    public ArrayList<CategoryApi> subcategories;
}
