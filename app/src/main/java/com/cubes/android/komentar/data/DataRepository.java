package com.cubes.android.komentar.data;


import android.util.Log;

import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.NewsCommentInsert;
import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.data.source.remote.networking.NewsApi;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoriesResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoryResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.CommentsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.HomePageResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.TagResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    private static DataRepository instance;
    private NewsApi api;

    private DataRepository() {
        api = NewsApi.getInstance();
    }

    public static DataRepository getInstance() {

        if (instance == null) {
            instance = new DataRepository();
        }

        return instance;
    }

    public interface VideosResponseListener {
        void onVideosResponse(NewsResponseModel.NewsDataResponseModel response);

        void onVideosFailure(Throwable t);
    }


    public void getVideosFromApi(int page, VideosResponseListener videosResponseListener) {

        api.getNewsService().getVideos(page).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {

                    videosResponseListener.onVideosResponse(response.body().data);

                }

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

                videosResponseListener.onVideosFailure(t);

            }
        });
    }

    public interface LatestResponseListener {
        void onResponse(NewsResponseModel.NewsDataResponseModel response);

        void onFailure(Throwable t);
    }

    public void getLatest(int page, LatestResponseListener latestResponseListener) {

        api.getNewsService().getLatest(page, 50).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {

                    latestResponseListener.onResponse(response.body().data);

                }

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

                latestResponseListener.onFailure(t);
            }
        });

    }

    public interface CategoryNewsResponseListener {
        void onResponse(CategoryResponseModel.CategoryDataResponseModel response);

        void onFailure(Throwable t);
    }

    public void getNewsForCategory(int categoryId, int page, CategoryNewsResponseListener listener) {

        Log.d("TAG", "getLatest: " + page);

        api.getNewsService().getCategoryNews(categoryId, page, 20).enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {


                    listener.onResponse(response.body().data);

                }

            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {

                listener.onFailure(t);
            }
        });
    }

    public interface SearchResponseListener {
        void onResponse(NewsResponseModel.NewsDataResponseModel response);

        void onFailure(Throwable t);
    }

    public void searchNews(String searchTerm, int page, SearchResponseListener searchResponseListener) {

        api.getNewsService().searchNews(searchTerm, page).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    searchResponseListener.onResponse(response.body().data);

                }

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

                searchResponseListener.onFailure(t);

            }
        });
    }

    public interface HomeResponseListener {
        void onResponse(HomePageResponseModel.HomePageDataResponseModel response);

        void onFailure(Throwable t);
    }

    public void getHomeNews(HomeResponseListener listener) {

        api.getNewsService().getHomePageData().enqueue(new Callback<HomePageResponseModel>() {
            @Override
            public void onResponse(Call<HomePageResponseModel> call, Response<HomePageResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    listener.onResponse(response.body().data);

                }

            }

            @Override
            public void onFailure(Call<HomePageResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public interface DetailResponseListener {
        void onResponse(NewsDetailsResponseModel.NewsDetailsDataResponseModel response);

        void onFailure(Throwable t);
    }

    public void sendNewsDetailsRequest(int newsId, DetailResponseListener listener) {


        api.getNewsService().getNewsDetails(newsId).enqueue(new Callback<NewsDetailsResponseModel>() {
            @Override
            public void onResponse(Call<NewsDetailsResponseModel> call, Response<NewsDetailsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    listener.onResponse(response.body().data);

                }

            }

            @Override
            public void onFailure(Call<NewsDetailsResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });
    }

    public interface TagResponseListener {
        void onResponse(TagResponseModel.TagDataResponseModel response);

        void onFailure(Throwable t);
    }

    public void sendTagRequest(int tagId, int page, TagResponseListener listener) {

        api.getNewsService().getTag(tagId, page).enqueue(new Callback<TagResponseModel>() {
            @Override
            public void onResponse(Call<TagResponseModel> call, Response<TagResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {

                    listener.onResponse(response.body().data);

                }
            }

            @Override
            public void onFailure(Call<TagResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });
    }

    public interface CommentsResponseListener {
        void onResponse(ArrayList<NewsComment> response);

        void onFailure(Throwable t);
    }

    public void getComments(int news_id, CommentsResponseListener listener) {
        api.getNewsService().getComments(news_id).enqueue(new Callback<CommentsResponseModel>() {
            @Override
            public void onResponse(Call<CommentsResponseModel> call, Response<CommentsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    listener.onResponse(response.body().data);

                }
            }

            @Override
            public void onFailure(Call<CommentsResponseModel> call, Throwable t) {

                listener.onFailure(t);
            }
        });
    }

    public interface CommentsVoteListener {
        void onResponse(NewsCommentVote response);

        void onFailure(Throwable t);
    }

    public void likeComment(int id, boolean vote, CommentsVoteListener listener) {

        api.getNewsService().postLike(id, vote)
                .enqueue(new Callback<NewsCommentVote>() {
                    @Override
                    public void onResponse(Call<NewsCommentVote> call, Response<NewsCommentVote> response) {
                        if (response.isSuccessful()) {

                            listener.onResponse(response.body());

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<NewsCommentVote> call, Throwable t) {

                        listener.onFailure(t);

                    }
                });

    }

    public void dislikeComment(int id, boolean vote, CommentsVoteListener listener) {

        api.getNewsService().postDislike(id, vote)
                .enqueue(new Callback<NewsCommentVote>() {
                    @Override
                    public void onResponse(Call<NewsCommentVote> call, Response<NewsCommentVote> response) {
                        if (response.isSuccessful()) {

                            listener.onResponse(response.body());

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<NewsCommentVote> call, Throwable t) {

                        listener.onFailure(t);

                    }
                });

    }

    public interface CategoriesResponseListener {
        void onResponse(ArrayList<Category> categories);

        void onFailure(Throwable t);
    }

    public void getAllCategories(CategoriesResponseListener listener) {

        api.getNewsService().getCategories().enqueue(new Callback<CategoriesResponseModel>() {
            @Override
            public void onResponse(Call<CategoriesResponseModel> call, Response<CategoriesResponseModel> response) {

                listener.onResponse(response.body().data);

            }

            @Override
            public void onFailure(Call<CategoriesResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public interface PostCommentResponseListener {
        void onResponse(NewsCommentInsert response);

        void onFailure(Throwable t);
    }

    public void postComment(NewsCommentInsert newsCommentInsert, PostCommentResponseListener listener) {

        NewsApi.getInstance().getNewsService().postComment(newsCommentInsert).enqueue(new Callback<NewsCommentInsert>() {
            @Override
            public void onResponse(Call<NewsCommentInsert> call, Response<NewsCommentInsert> response) {

                listener.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<NewsCommentInsert> call, Throwable t) {

                listener.onFailure(t);

            }
        });
    }

}
