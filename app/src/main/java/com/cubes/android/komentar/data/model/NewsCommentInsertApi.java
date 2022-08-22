package com.cubes.android.komentar.data.model;

public class NewsCommentInsertApi {
    public int news;
    public int reply_id;
    public String name;
    public String email;
    public String content;

    public NewsCommentInsertApi() {
    }

    public NewsCommentInsertApi(int news, int reply_id, String name, String email, String content) {
        this.news = news;
        this.reply_id = reply_id;
        this.name = name;
        this.email = email;
        this.content = content;
    }
}
