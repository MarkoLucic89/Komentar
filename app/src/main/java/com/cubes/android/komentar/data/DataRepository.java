package com.cubes.android.komentar.data;


import android.util.Log;

import com.cubes.android.komentar.data.source.remote.networking.NewsService;
import com.cubes.android.komentar.di.LocalDataSource;
import com.cubes.android.komentar.di.RemoteDataSource;
import com.cubes.android.komentar.data.model.CategoryApi;
import com.cubes.android.komentar.data.model.NewsApi;
import com.cubes.android.komentar.data.model.NewsCommentApi;
import com.cubes.android.komentar.data.model.NewsCommentInsertApi;
import com.cubes.android.komentar.data.model.NewsTagApi;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.data.model.domain.HomePageData;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.model.domain.NewsComment;
import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.cubes.android.komentar.data.model.domain.NewsDetails;
import com.cubes.android.komentar.data.model.domain.NewsTag;
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

    private static final String TAG = "DataRepository";

    private NewsService api;

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    public DataRepository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

        this.api = remoteDataSource.newsService;
    }

    private ArrayList<News> mapNewsFromResponse(ArrayList<NewsApi> newsFromResponse) {

        ArrayList<News> newsList = new ArrayList<>();

        for (NewsApi newsItemApi : newsFromResponse) {

            News news = new News();

            news.id = newsItemApi.id;
            news.title = newsItemApi.title;
            news.image = newsItemApi.image;
            news.title = newsItemApi.title;
            news.createdAt = newsItemApi.created_at;
            news.url = newsItemApi.url;

            news.category = mapCategoryFromResponse(newsItemApi.category);

            newsList.add(news);

        }

        return newsList;
    }

    private Category mapCategoryFromResponse(CategoryApi categoryApi) {

        Category category = new Category();

        category.id = categoryApi.id;
        category.name = categoryApi.name;
        category.type = categoryApi.type;
        category.name = categoryApi.name;
        category.color = categoryApi.color;

        ArrayList<Category> subcategories = new ArrayList<>();

        if (categoryApi.subcategories != null && !categoryApi.subcategories.isEmpty()) {


            for (CategoryApi subcategoryApi : categoryApi.subcategories) {

                Category subcategory = new Category();

                subcategory.id = subcategoryApi.id;
                subcategory.type = subcategoryApi.type;
                subcategory.name = subcategoryApi.name;
                subcategory.color = subcategoryApi.color;

                subcategories.add(subcategory);

            }

        }

        category.subcategories = subcategories;

        return category;
    }


    public interface VideosResponseListener {

        void onVideosResponse(ArrayList<News> newsList, boolean hasNextPage);

        void onVideosFailure(Throwable t);

    }


    public void getVideos(int page, VideosResponseListener videosResponseListener) {

        api.getVideos(page).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && response.body().data.news != null
                ) {

                    ArrayList<News> videoNews = mapNewsFromResponse(response.body().data.news);

                    boolean hasMorePages = response.body().data.pagination.has_more_pages;

                    videosResponseListener.onVideosResponse(videoNews, hasMorePages);

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

    public void getLatestNews(int page, LatestResponseListener latestResponseListener) {

        api.getLatest(page, 50).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && response.body().data.news != null
                ) {

                    ArrayList<News> latestNews = mapNewsFromResponse(response.body().data.news);

                    boolean hasMorePages = response.body().data.pagination.has_more_pages;

                    latestResponseListener.onResponse(latestNews, hasMorePages);

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

    public void getNewsForCategory(int categoryId, int page, CategoryNewsResponseListener listener) {

        Log.d("TAG", "getLatest: " + page);

        api.getCategoryNews(categoryId, page, 20).enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && response.body().data.news != null
                ) {

                    ArrayList<News> categoryNews = mapNewsFromResponse(response.body().data.news);

                    boolean hasMorePages = response.body().data.pagination.has_more_pages;

                    listener.onResponse(categoryNews, hasMorePages);

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

    public void searchNews(String searchTerm, int page, SearchResponseListener searchResponseListener) {

        api.searchNews(searchTerm, page).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && response.body().data.news != null
                ) {

                    ArrayList<News> searchNews = mapNewsFromResponse(response.body().data.news);

                    boolean hasMorePages = response.body().data.pagination.has_more_pages;

                    searchResponseListener.onResponse(searchNews, hasMorePages);

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

    public void getHomeNews(HomeResponseListener listener) {

        api.getHomePageData().enqueue(new Callback<HomePageResponseModel>() {
            @Override
            public void onResponse(Call<HomePageResponseModel> call, Response<HomePageResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    HomePageData homePageData = mapHomePageDataFromResponse(response.body().data);

                    listener.onResponse(homePageData);

                }

            }

            @Override
            public void onFailure(Call<HomePageResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });

    }

    private HomePageData mapHomePageDataFromResponse(HomePageResponseModel.HomePageDataResponseModel homePageDataApi) {

        HomePageData homePageData = new HomePageData();

        homePageData.slider = mapNewsFromResponse(homePageDataApi.slider);
        homePageData.top = mapNewsFromResponse(homePageDataApi.top);
        homePageData.editorsChoice = mapNewsFromResponse(homePageDataApi.editors_choice);
        homePageData.latest = mapNewsFromResponse(homePageDataApi.latest);
        homePageData.mostCommented = mapNewsFromResponse(homePageDataApi.most_comented);
        homePageData.mostRead = mapNewsFromResponse(homePageDataApi.most_read);
        homePageData.videos = mapNewsFromResponse(homePageDataApi.videos);

        ArrayList<HomePageData.CategoryBox> categoryBoxes = new ArrayList<>();

        for (HomePageResponseModel.CategoryBoxResponseModel model : homePageDataApi.category) {

            HomePageData.CategoryBox categoryBox = new HomePageData.CategoryBox();

            categoryBox.news = mapNewsFromResponse(model.news);
            categoryBox.color = model.color;
            categoryBox.id = model.id;
            categoryBox.title = model.title;

            categoryBoxes.add(categoryBox);

        }

        homePageData.category = categoryBoxes;

        return homePageData;
    }

    public interface DetailResponseListener {
        void onResponse(NewsDetails newsDetails);

        void onFailure(Throwable t);
    }

    public void getNewsDetails(int newsId, DetailResponseListener listener) {


        api.getNewsDetails(newsId).enqueue(new Callback<NewsDetailsResponseModel>() {
            @Override
            public void onResponse(Call<NewsDetailsResponseModel> call, Response<NewsDetailsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    NewsDetails newsDetails = mapNewsDetailsFromApi(response.body().data);

                    listener.onResponse(newsDetails);

                }

            }

            @Override
            public void onFailure(Call<NewsDetailsResponseModel> call, Throwable t) {

                listener.onFailure(t);

            }
        });
    }

    private NewsDetails mapNewsDetailsFromApi(NewsDetailsResponseModel.NewsDetailsDataResponseModel newsDetailsApi) {

        NewsDetails newsDetails = new NewsDetails();

        newsDetails.id = newsDetailsApi.id;
        newsDetails.title = newsDetailsApi.title;
        newsDetails.url = newsDetailsApi.url;
        newsDetails.tags = mapTagsFromResponse(newsDetailsApi.tags);
        newsDetails.commentsTop = mapCommentsFromResponse(newsDetailsApi.comments_top_n);

        if (newsDetailsApi.comment_enabled == 1) {
            newsDetails.commentEnabled = true;
        } else {
            newsDetails.commentEnabled = false;
        }

        newsDetails.commentsCount = newsDetailsApi.comments_count;
        newsDetails.relatedNews = mapNewsFromResponse(newsDetailsApi.related_news);

        return newsDetails;
    }

    private ArrayList<NewsTag> mapTagsFromResponse(ArrayList<NewsTagApi> newsTagsApi) {

        ArrayList<NewsTag> newsTags = new ArrayList<>();

        if (newsTagsApi != null && !newsTagsApi.isEmpty()) {

            for (NewsTagApi newsTagApi : newsTagsApi) {

                NewsTag newsTag = new NewsTag();

                newsTag.id = newsTagApi.id;
                newsTag.title = newsTagApi.title;

                newsTags.add(newsTag);
            }
        }

        return newsTags;
    }

    public interface TagResponseListener {
        void onResponse(ArrayList<News> newsList, boolean hasMorePages);

        void onFailure(Throwable t);
    }

    public void getNewsForTag(int tagId, int page, TagResponseListener listener) {

        api.getTag(tagId, page).enqueue(new Callback<TagResponseModel>() {
            @Override
            public void onResponse(Call<TagResponseModel> call, Response<TagResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null
                        && response.body().data.news != null
                ) {

                    ArrayList<News> tagNews = mapNewsFromResponse(response.body().data.news);

                    boolean hasMorePages = response.body().data.pagination.has_more_pages;

                    listener.onResponse(tagNews, hasMorePages);

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

        api.getComments(news_id).enqueue(new Callback<CommentsResponseModel>() {
            @Override
            public void onResponse(Call<CommentsResponseModel> call, Response<CommentsResponseModel> response) {

                if (response.body() != null
                        && response.isSuccessful()
                        && response.body().data != null) {

                    ArrayList<NewsComment> comments = mapCommentsFromResponse(response.body().data);

                    listener.onResponse(comments);

                }
            }

            @Override
            public void onFailure(Call<CommentsResponseModel> call, Throwable t) {

                listener.onFailure(t);
            }
        });
    }

    private ArrayList<NewsComment> mapCommentsFromResponse(ArrayList<NewsCommentApi> commentsApi) {

        ArrayList<NewsComment> comments = new ArrayList<>();

        if (commentsApi != null && !commentsApi.isEmpty()) {

            for (NewsCommentApi commentApi : commentsApi) {

                NewsComment comment = new NewsComment();

                comment.id = commentApi.id;
                comment.negativeVotes = commentApi.negative_votes;
                comment.positiveVotes = commentApi.positive_votes;
                comment.createdAt = commentApi.created_at;
                comment.newsId = commentApi.news;
                comment.name = commentApi.name;
                comment.parentCommentId = commentApi.parent_comment;
                comment.content = commentApi.content;

                if (commentApi.children != null && !commentApi.children.isEmpty()) {
                    comment.children = mapCommentsFromResponse(commentApi.children);
                } else {
                    comment.children = new ArrayList<>();
                }

                comments.add(comment);

            }

        }

        return comments;
    }

    public interface CommentsVoteListener {
        void onResponse(NewsCommentVote response);

        void onFailure(Throwable t);
    }

    public void likeComment(int id, boolean vote, CommentsVoteListener listener) {

        api.postLike(id, vote)
                .enqueue(new Callback<NewsCommentVote>() {
                    @Override
                    public void onResponse(Call<NewsCommentVote> call, Response<NewsCommentVote> response) {
                        if (response.isSuccessful()) {

                            listener.onResponse(response.body());

                        } else {

                            Log.d(TAG, "onResponse: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsCommentVote> call, Throwable t) {

                        listener.onFailure(t);

                    }
                });

    }

    public void dislikeComment(int id, boolean vote, CommentsVoteListener listener) {

        api.postDislike(id, vote)
                .enqueue(new Callback<NewsCommentVote>() {
                    @Override
                    public void onResponse(Call<NewsCommentVote> call, Response<NewsCommentVote> response) {

                        if (response.isSuccessful()) {

                            listener.onResponse(response.body());

                        } else {

                            Log.d(TAG, "onResponse: " + response.errorBody());

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

        api.getCategories().enqueue(new Callback<CategoriesResponseModel>() {
            @Override
            public void onResponse(Call<CategoriesResponseModel> call, Response<CategoriesResponseModel> response) {

                ArrayList<Category> categories = new ArrayList<>();

                for (CategoryApi categoryApi : response.body().data) {

                    Category category = mapCategoryFromResponse(categoryApi);

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

    public void postComment(NewsCommentInsertApi newsCommentInsert, PostCommentResponseListener listener) {

        api.postComment(newsCommentInsert).enqueue(new Callback<NewsCommentInsertApi>() {
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
