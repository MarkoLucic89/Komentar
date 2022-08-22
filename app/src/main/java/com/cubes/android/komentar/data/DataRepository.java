package com.cubes.android.komentar.data;


import android.util.Log;

import com.cubes.android.komentar.data.model.CategoryApi;
import com.cubes.android.komentar.data.model.domain.HomePageData;
import com.cubes.android.komentar.data.model.domain.NewsComment;
import com.cubes.android.komentar.data.model.NewsCommentInsertApi;
import com.cubes.android.komentar.data.model.NewsApi;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.cubes.android.komentar.data.model.domain.NewsDetails;
import com.cubes.android.komentar.data.source.remote.networking.NewsRetrofit;
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
    private NewsRetrofit api;

    private DataRepository() {
        api = NewsRetrofit.getInstance();
    }

    public static DataRepository getInstance() {

        if (instance == null) {
            instance = new DataRepository();
        }

        return instance;
    }

    private ArrayList<News> mapNewsFromResponse(ArrayList<NewsApi> newsFromResponse) {

        ArrayList<News> newsList = new ArrayList<>();

        for (NewsApi newsItemApi : newsFromResponse) {

            News news = new News();

            news.id = newsItemApi.id;
            news.image = newsItemApi.image;
            news.title = newsItemApi.title;
            news.created_at = newsItemApi.created_at;
            news.url = newsItemApi.url;

            Category category = new Category();

            category.id = newsItemApi.category.id;
            category.type = newsItemApi.category.type;
            category.name = newsItemApi.category.name;
            category.color = newsItemApi.category.color;

            ArrayList<Category> subcategories = new ArrayList<>();

            if (newsItemApi.category.subcategories != null && !newsItemApi.category.subcategories.isEmpty()) {


                for (CategoryApi subcategoryApi : newsItemApi.category.subcategories) {

                    Category subcategory = new Category();

                    subcategory.id = subcategoryApi.id;
                    subcategory.type = subcategoryApi.type;
                    subcategory.name = subcategoryApi.name;
                    subcategory.color = subcategoryApi.color;

                    subcategories.add(subcategory);

                }

            }


            category.subcategories = subcategories;

            news.category = category;

            newsList.add(news);

        }

        return newsList;
    }


    public interface VideosResponseListener {

        void onVideosResponse(ArrayList<News> newsList, boolean hasNextPage);
        void onVideosFailure(Throwable t);

    }


    public void getVideosRequest(int page, VideosResponseListener videosResponseListener) {

        api.getNewsService().getVideos(page).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {

                    videosResponseListener.onVideosResponse(mapNewsFromResponse(response.body().data.news), response.body().data.pagination.has_more_pages);

                }

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

                videosResponseListener.onVideosFailure(t);

            }
        });
    }

    public interface LatestResponseListener {
        void onResponse(ArrayList<News> newsList, boolean hasMorePages);

        void onFailure(Throwable t);
    }

    public void getLatestNewsRequest(int page, LatestResponseListener latestResponseListener) {

        api.getNewsService().getLatest(page, 50).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {

                    latestResponseListener.onResponse(mapNewsFromResponse(response.body().data.news), response.body().data.pagination.has_more_pages);

                }

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

                latestResponseListener.onFailure(t);
            }
        });

    }

    public interface CategoryNewsResponseListener {
        void onResponse(ArrayList<News> newsList, boolean hasMorePages);

        void onFailure(Throwable t);
    }

    public void getNewsForCategoryRequest(int categoryId, int page, CategoryNewsResponseListener listener) {

        Log.d("TAG", "getLatest: " + page);

        api.getNewsService().getCategoryNews(categoryId, page, 20).enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {


                    listener.onResponse(mapNewsFromResponse(response.body().data.news), response.body().data.pagination.has_more_pages);

                }

            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {

                listener.onFailure(t);
            }
        });
    }

    public interface SearchResponseListener {
        void onResponse(ArrayList<News> newsList, boolean hasMorePages);

        void onFailure(Throwable t);
    }

    public void searchNewsRequest(String searchTerm, int page, SearchResponseListener searchResponseListener) {

        api.getNewsService().searchNews(searchTerm, page).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    searchResponseListener.onResponse(mapNewsFromResponse(response.body().data.news), response.body().data.pagination.has_more_pages);

                }

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

                searchResponseListener.onFailure(t);

            }
        });
    }

    public interface HomeResponseListener {
        void onResponse(HomePageData response);

        void onFailure(Throwable t);
    }

    public void getHomeNewsRequest(HomeResponseListener listener) {

        api.getNewsService().getHomePageData().enqueue(new Callback<HomePageResponseModel>() {
            @Override
            public void onResponse(Call<HomePageResponseModel> call, Response<HomePageResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    HomePageData homePageData = new HomePageData();

                    homePageData.slider = mapNewsFromResponse(response.body().data.slider);
                    homePageData.top = mapNewsFromResponse(response.body().data.top);
                    homePageData.editors_choice = mapNewsFromResponse(response.body().data.editors_choice);
                    homePageData.latest = mapNewsFromResponse(response.body().data.latest);
                    homePageData.most_commented = mapNewsFromResponse(response.body().data.most_comented);
                    homePageData.most_read = mapNewsFromResponse(response.body().data.most_read);
                    homePageData.videos = mapNewsFromResponse(response.body().data.videos);

                    ArrayList<HomePageData.CategoryBox> categoryBoxes = new ArrayList<>();

                    for (HomePageResponseModel.CategoryBoxResponseModel model : response.body().data.category) {

                        HomePageData.CategoryBox categoryBox = new HomePageData.CategoryBox();

                        categoryBox.news = mapNewsFromResponse(model.news);
                        categoryBox.color = model.color;
                        categoryBox.id = model.id;
                        categoryBox.title = model.title;

                        categoryBoxes.add(categoryBox);

                    }

                    homePageData.category = categoryBoxes;



                    listener.onResponse(homePageData);

                }

            }

            @Override
            public void onFailure(Call<HomePageResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public interface DetailResponseListener {
        void onResponse(NewsDetails newsDetails);

        void onFailure(Throwable t);
    }

    public void getNewsDetailsRequest(int newsId, DetailResponseListener listener) {


        api.getNewsService().getNewsDetails(newsId).enqueue(new Callback<NewsDetailsResponseModel>() {
            @Override
            public void onResponse(Call<NewsDetailsResponseModel> call, Response<NewsDetailsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    NewsDetails newsDetails = new NewsDetails();

                    newsDetails.id = response.body().data.id;
                    newsDetails.url = response.body().data.url;
                    newsDetails.tags = response.body().data.tags;
                    newsDetails.comments_top_n = response.body().data.comments_top_n;
                    if (response.body().data.comment_enabled == 1) {
                        newsDetails.comment_enabled = true;
                    } else {
                        newsDetails.comment_enabled = false;
                    }
                    newsDetails.comments_count = response.body().data.comments_count;
                    newsDetails.related_news = response.body().data.related_news;

                    listener.onResponse(newsDetails);

                }

            }

            @Override
            public void onFailure(Call<NewsDetailsResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });
    }

    public interface TagResponseListener {
        void onResponse(ArrayList<News> newsList, boolean hasMorePages);

        void onFailure(Throwable t);
    }

    public void getNewsForTagRequest(int tagId, int page, TagResponseListener listener) {

        api.getNewsService().getTag(tagId, page).enqueue(new Callback<TagResponseModel>() {
            @Override
            public void onResponse(Call<TagResponseModel> call, Response<TagResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && !response.body().data.news.isEmpty()) {

                    listener.onResponse(mapNewsFromResponse(response.body().data.news), response.body().data.pagination.has_more_pages);

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

    public void getCommentsRequest(int news_id, CommentsResponseListener listener) {

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

    public void likeCommentRequest(int id, boolean vote, CommentsVoteListener listener) {

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

    public void dislikeCommentRequest(int id, boolean vote, CommentsVoteListener listener) {

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

    public void getAllCategoriesRequest(CategoriesResponseListener listener) {

        api.getNewsService().getCategories().enqueue(new Callback<CategoriesResponseModel>() {
            @Override
            public void onResponse(Call<CategoriesResponseModel> call, Response<CategoriesResponseModel> response) {

                ArrayList<Category> categories = new ArrayList<>();

                for (CategoryApi categoryApi : response.body().data) {

                    Category category = new Category();

                    category.id = categoryApi.id;
                    category.type = categoryApi.type;
                    category.name = categoryApi.name;
                    category.color = categoryApi.color;

                    if (category.subcategories != null && !category.subcategories.isEmpty()) {

                        for (CategoryApi subcategoryApi : categoryApi.subcategories) {
                            Category subcategory = new Category();

                            subcategory.id = subcategoryApi.id;
                            subcategory.type = subcategoryApi.type;
                            subcategory.name = subcategoryApi.name;
                            subcategory.color = subcategoryApi.color;

                            category.subcategories.add(subcategory);

                        }

                    }

                    categories.add(category);

                }

                listener.onResponse(categories);

            }

            @Override
            public void onFailure(Call<CategoriesResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    public interface PostCommentResponseListener {
        void onResponse(NewsCommentInsertApi response);

        void onFailure(Throwable t);
    }

    public void postCommentRequest(NewsCommentInsertApi newsCommentInsert, PostCommentResponseListener listener) {

        NewsRetrofit.getInstance().getNewsService().postComment(newsCommentInsert).enqueue(new Callback<NewsCommentInsertApi>() {
            @Override
            public void onResponse(Call<NewsCommentInsertApi> call, Response<NewsCommentInsertApi> response) {

                listener.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<NewsCommentInsertApi> call, Throwable t) {

                listener.onFailure(t);

            }
        });
    }

}
