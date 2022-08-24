package com.cubes.android.komentar.data.model.domain;

import java.util.ArrayList;

public class HomePageData {

    public ArrayList<News> slider;
    public ArrayList<News> top;
    public ArrayList<News> editorsChoice;
    public ArrayList<News> latest;
    public ArrayList<News> mostRead;
    public ArrayList<News> mostCommented;
    public ArrayList<News> videos;
    public ArrayList<CategoryBox> category;

    public static class CategoryBox {
        public int id;
        public String title;
        public String color;
        public ArrayList<News> news;
    }

}




