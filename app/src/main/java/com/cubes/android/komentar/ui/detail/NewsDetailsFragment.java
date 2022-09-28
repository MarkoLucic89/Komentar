package com.cubes.android.komentar.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.model.domain.NewsComment;
import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.cubes.android.komentar.data.model.domain.NewsDetails;
import com.cubes.android.komentar.data.model.domain.NewsTag;
import com.cubes.android.komentar.data.source.local.SharedPrefs;
import com.cubes.android.komentar.data.source.local.database.dao.NewsBookmarksDao;
import com.cubes.android.komentar.databinding.FragmentNewsDetailsBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.post_comment.PostCommentActivity;
import com.cubes.android.komentar.ui.tag.TagActivity;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
        TRENUTNO SE NE KORISTI!!!
 */

public class NewsDetailsFragment extends Fragment implements
        CommentsAdapter.CommentsListener,
        NewsDetailsTagsAdapter.TagListener,
        NewsListener {

    private static final String TAG = "NewsDetailsFragment";

    private FragmentNewsDetailsBinding binding;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String NEWS_ID = "news_id";

    private int mNewsId;
    private String mNewsUrl;

    private DetailsListener listener;

    private ArrayList<NewsCommentVote> mVotes = new ArrayList<>();

    private DataRepository dataRepository;

    private CommentsAdapter commentsAdapter;

    private NewsBookmarksDao bookmarksDao;

    private ArrayList<News> bookmarks = new ArrayList<>();

    private News mTempNews = null;


    public interface DetailsListener {

        void onDetailsResponseListener(int newsId, String newsUrl, News bookmark);

    }


    public NewsDetailsFragment() {
        // Required empty public constructor
    }


    public static NewsDetailsFragment newInstance(int newsId) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(NEWS_ID, newsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (DetailsListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNewsId = getArguments().getInt(NEWS_ID);
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;

        bookmarksDao = appContainer.room.bookmarksDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nestedScrollView.setVisibility(View.GONE);

//        binding.nestedScrollView.setSmoothScrollingEnabled(true);

        getNewsDetails();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTempNews != null) {
            listener.onDetailsResponseListener(mNewsId, mNewsUrl, mTempNews);
        }
    }

    private void getNewsDetails() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.swipeRefreshLayout.setRefreshing(true);

        dataRepository.getNewsDetails(mNewsId, new DataRepository.DetailResponseListener() {
            @Override
            public void onResponse(NewsDetails newsDetails) {


                mNewsId = newsDetails.id;
                mNewsUrl = newsDetails.url;
                mTempNews = initNewsObject(newsDetails);

                Bundle bundle = new Bundle();
                bundle.putString("Vest", newsDetails.title);
                mFirebaseAnalytics.logEvent("android_komentar", bundle);

                //Room
                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                service.execute(() -> {

                    //doInBackgroundThread
                    bookmarks.clear();
                    bookmarks.addAll(bookmarksDao.getBookmarkNews());

                    //onPostExecute
                    handler.post(() -> {

                        for (News news : bookmarks) {
                            if (news.id == newsDetails.id) {
                                mTempNews.isInBookmarks = true;
                                break;
                            }
                        }

                        updateList(newsDetails);

                        setListeners(newsDetails);

                        Log.d(TAG, "onResponse: " + newsDetails.title);

                    });

                });

                service.shutdown();

            }

            @Override
            public void onFailure(Throwable t) {

                binding.imageViewRefresh.setVisibility(View.VISIBLE);

                binding.nestedScrollView.setVisibility(View.GONE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private News initNewsObject(NewsDetails newsDetails) {
        News news = new News();
        news.id = newsDetails.id;
        news.image = newsDetails.image;
        news.categoryName = newsDetails.category.name;
        news.categoryColor = newsDetails.category.color;
        news.title = newsDetails.title;
        news.createdAt = newsDetails.createdAt;
        news.url = newsDetails.url;
        return news;
    }

    private void setListeners(NewsDetails newsDetails) {

        binding.buttonAllComments.setOnClickListener(view -> goToCommentsActivity(newsDetails.id));
        binding.buttonPostComment.setOnClickListener(view -> goOnPostCommentActivity(newsDetails.id, 0));
        binding.imageViewRefresh.setOnClickListener(view1 -> {
            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
            getNewsDetails();
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> getNewsDetails());

    }

    private void updateList(NewsDetails newsDetails) {

        String url = "https://komentar.rs/wp-json/api/newswebview?id=" + newsDetails.id + "&version=2";

        binding.webView.loadUrl(url);

//        binding.webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url) {

                AdRequest adRequest1 = new AdRequest.Builder().build();

                binding.adView1.loadAd(adRequest1);

                AdRequest adRequest2 = new AdRequest.Builder().build();
                binding.adView2.loadAd(adRequest2);

                initTagAdapter(newsDetails.tags);

                binding.textViewTitleComments.setText("KOMENTARI (" + newsDetails.commentsCount + ")");
                binding.buttonAllComments.setText("SVI KOMENTARI (" + newsDetails.commentsCount + ")");

                if (newsDetails.commentsCount == 0) {
                    binding.buttonAllComments.setVisibility(View.GONE);
                }

                if (newsDetails.commentsTop != null && !newsDetails.commentsTop.isEmpty()) {
                    getCommentVotes(newsDetails.commentsTop);
                }

                initCommentsAdapter(newsDetails.commentsTop);

                /*
                ZA TESTIRANJE KOMENTARA
                 */

//                dataRepository.getComments(newsDetails.id, new DataRepository.CommentsResponseListener() {
//                    @Override
//                    public void onResponse(ArrayList<NewsComment> response) {
//
//                        ArrayList<NewsComment> comments = response;
//
//                        if (comments != null && !comments.isEmpty()) {
//                            getCommentVotes(comments);
//                        }
//
//                        initCommentsAdapter(comments);
//
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//
//                    }
//                });

                MyMethodsClass.checkBookmarks(newsDetails.relatedNews, bookmarks);

                initRelatedNewsAdapter(newsDetails.relatedNews);

                AdRequest adRequest3 = new AdRequest.Builder().build();
                binding.adView3.loadAd(adRequest3);

                binding.nestedScrollView.setVisibility(View.VISIBLE);
                binding.swipeRefreshLayout.setRefreshing(false);

                listener.onDetailsResponseListener(mNewsId, mNewsUrl, mTempNews);

            }
        });

    }


    private void initRelatedNewsAdapter(ArrayList<News> relatedNews) {

        binding.recyclerViewRelatedNews.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRelatedNews.setAdapter(new NewsDetailsRelatedNewsAdapter(relatedNews, this));

    }

    private void initCommentsAdapter(ArrayList<NewsComment> commentsTop) {
        binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsAdapter = new CommentsAdapter(this, commentsTop);
        binding.recyclerViewComments.setAdapter(commentsAdapter);
    }

    private void initTagAdapter(ArrayList<NewsTag> tags) {

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        binding.recyclerViewTags.setLayoutManager(flexboxLayoutManager);
        binding.recyclerViewTags.setAdapter(new NewsDetailsTagsAdapter(this, tags));

    }

    private void getCommentVotes(ArrayList<NewsComment> comments) {

        //Room

//        ExecutorService service = Executors.newSingleThreadExecutor();
//        Handler handler = new Handler(Looper.getMainLooper());
//        service.execute(() -> {
//
//            //doInBackgroundThread
//            List<NewsCommentVote> votes = NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().getNewsCommentVotes();
//
//            //onPostExecute
//            handler.post(() -> checkVotedComments(comments, votes));
//
//        });

        //SHARED PREFS

        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            ArrayList<NewsCommentVote> votes = (ArrayList<NewsCommentVote>) SharedPrefs.readListFromPref(getActivity());

            if (votes != null) {
                mVotes.addAll(votes);
            }

            //onPostExecute
            handler.post(() -> {
                checkVotedComments(comments, mVotes);
            });

        });

    }

    private void checkVotedComments(ArrayList<NewsComment> comments, List<NewsCommentVote> votes) {

        for (NewsCommentVote vote : votes) {

            for (NewsComment comment : comments) {

                if (vote.id.equals(comment.id)) {
                    comment.newsCommentVote = vote;
                }

                checkChildrenVotes(comment.children, vote);

            }
        }

        commentsAdapter.updateList(comments);

    }

    private void checkChildrenVotes(ArrayList<NewsComment> children, NewsCommentVote vote) {

        for (NewsComment subComment : children) {

            if (vote.id.equals(subComment.id)) {
                subComment.newsCommentVote = vote;
            }

            if (subComment.children != null) {
                checkChildrenVotes(subComment.children, vote);
            }

        }
    }


    @Override
    public void onLikeListener(int id, boolean vote) {

        dataRepository.likeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                //ROOM

//                ExecutorService service = Executors.newSingleThreadExecutor();
//                Handler handler = new Handler(Looper.getMainLooper());
//                service.execute(() -> {
//
//                    //doInBackgroundThread
//                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
//                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);
//
//                    //onPostExecute
//                    handler.post(() -> adapter.commentLiked(id, vote));
//                });
//
//                service.shutdown();


                //SHARED PREFS

                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
                    mVotes.add(newsCommentVote);
                    SharedPrefs.writeListInPref(getActivity(), mVotes);

                    //onPostExecute
                    handler.post(() -> commentsAdapter.commentLiked(id, vote));

                });

                service.shutdown();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Komentar nije izglasan, došlo je do greške.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDislikeListener(int id, boolean vote) {

        dataRepository.dislikeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                //ROOM

//                ExecutorService service = Executors.newSingleThreadExecutor();
//                Handler handler = new Handler(Looper.getMainLooper());
//                service.execute(() -> {
//
//                    //doInBackgroundThread
//                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), false);
//                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);
//
//                    //onPostExecute
//                    handler.post(() -> adapter.commentDisliked(id, vote));
//
//                });
//
//                service.shutdown();

                //SHARED PREFS

                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
                    mVotes.add(newsCommentVote);
                    SharedPrefs.writeListInPref(getActivity(), mVotes);

                    //onPostExecute
                    handler.post(() -> commentsAdapter.commentDisliked(id, vote));

                });

                service.shutdown();
            }


            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Komentar nije izglasan, došlo je do greške.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void goOnPostCommentActivity(int newsId, int reply_id) {
        Intent intent = new Intent(getContext(), PostCommentActivity.class);
        intent.putExtra("news", newsId);
        intent.putExtra("reply_id", reply_id);
        startActivity(intent);
    }

    @Override
    public void goToCommentsActivity(int newsId) {

        Intent intent = new Intent(getContext(), CommentsActivity.class);
        intent.putExtra("news_id", newsId);
        startActivity(intent);
    }

    @Override
    public void onTagClicked(int tagId, String tagTitle) {
        Intent intent = new Intent(getContext(), TagActivity.class);
        intent.putExtra("tag_id", tagId);
        intent.putExtra("tag_title", tagTitle);
        startActivity(intent);
    }

    @Override
    public void onNewsClicked(int newsId, int[] newsIdList) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);

    }

    @Override
    public void onNewsMenuCommentsClicked(int newsId) {
        Intent intent = new Intent(getActivity(), CommentsActivity.class);
        intent.putExtra("news_id", newsId);
        getContext().startActivity(intent);
    }

    @Override
    public void onNewsMenuShareClicked(String url) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onNewsMenuFavoritesClicked(News news) {

        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            if (news.isInBookmarks) {
                bookmarksDao.delete(news);
            } else {
                bookmarksDao.insert(news);
            }

            //onPostExecute
            if (news.isInBookmarks) {
                handler.post(() -> Toast.makeText(getContext(), "Vest je uspešno uklonjena iz arhive", Toast.LENGTH_SHORT).show());
                news.isInBookmarks = false;
            } else {
                handler.post(() -> Toast.makeText(getContext(), "Vest je uspešno sačuvana u arhivu", Toast.LENGTH_SHORT).show());
                news.isInBookmarks = true;
            }

        });

        service.shutdown();
    }
}