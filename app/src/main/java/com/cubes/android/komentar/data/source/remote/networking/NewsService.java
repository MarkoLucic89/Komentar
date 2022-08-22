package com.cubes.android.komentar.data.source.remote.networking;

import com.cubes.android.komentar.data.model.NewsCommentInsertApi;
import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoriesResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.CommentsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.HomePageResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoryResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.TagResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsService {

    @GET("api/homepage")
    Call<HomePageResponseModel> getHomePageData();

    @GET("api/videos")
    Call<NewsResponseModel> getVideos(
            @Query("page") int page
    );

    @GET("api/latest")
    Call<NewsResponseModel> getLatest(
            @Query("page") int page,
            @Query("rows") int rows);

    @GET("api/search")
    Call<NewsResponseModel> searchNews(
            @Query("search_parameter") String searchParameter,
            @Query("page") int page
    );

    @GET("api/categories")
    Call<CategoriesResponseModel> getCategories();

    @GET("api/category/{id}")
    Call<CategoryResponseModel> getCategory(
            @Path("id") int id,
            @Query("page") int page
    );

    @GET("api/category/{id}")
    Call<CategoryResponseModel> getCategoryNews(
            @Path("id") int id,
            @Query("page") int page,
            @Query("size") int rows
    );

    @GET("api/newsdetails")
    Call<NewsDetailsResponseModel> getNewsDetails(
            @Query("id") int id
    );

    @GET("api/tag")
    Call<TagResponseModel> getTag(
            @Query("tag") int tag,
            @Query("page") int page
    );

    @GET("api/comments")
    Call<CommentsResponseModel> getComments(
            @Query("id") int id
    );

    @POST("api/commentinsert")
    Call<NewsCommentInsertApi> postComment(@Body NewsCommentInsertApi newsCommentInsert);

    @FormUrlEncoded
    @POST("api/commentinsert")
    Call<NewsCommentInsertApi> postComment(
            @Field("news") int news,
            @Field("reply_id") int reply_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("content") String content
    );

    @POST("api/commentvote")
    Call<NewsCommentVote> postVote(
            @Query("comment") int id,
            @Query("vote") boolean vote
    );

    @POST("api/commentvote")
    Call<NewsCommentVote> postLike(
            @Query("comment") int id,
            @Query("vote") boolean vote
    );

    @POST("api/commentvote")
    Call<NewsCommentVote> postDislike(
            @Query("comment") int id,
            @Query("downvote") boolean vote
    );


}
