package com.cubes.android.komentar.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.model.domain.NewsComment;
import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.cubes.android.komentar.data.model.domain.NewsDetails;
import com.cubes.android.komentar.data.model.domain.NewsTag;
import com.cubes.android.komentar.data.source.local.SharedPrefs;
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

    private int[] newsIdList;

    private DetailsListener listener;

    private ArrayList<NewsCommentVote> mVotes = new ArrayList<>();

    private DataRepository dataRepository;

    public interface DetailsListener {

        void onDetailsResponseListener(int newsId, String newsUrl);

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

//        binding.nestedScrollView.fullScroll(View.FOCUS_DOWN);
//        binding.nestedScrollView.fullScroll(View.FOCUS_UP);
//        binding.nestedScrollView.setSmoothScrollingEnabled(true);

        getNewsDetails();

//        binding.nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (view1, i, i1, i2, i3) -> {
//            int length = binding.nestedScrollView.getChildAt(0).getHeight() - binding.nestedScrollView.getHeight();
//
//            binding.progressBarScrollIndicator.setMax(length);
//            binding.progressBarScrollIndicator.setProgress(i1);
//        });


    }

    @Override
    public void onResume() {
        super.onResume();

        listener.onDetailsResponseListener(mNewsId, mNewsUrl);
    }

    private void getNewsDetails() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.swipeRefreshLayout.setRefreshing(true);

        dataRepository.getNewsDetails(mNewsId, new DataRepository.DetailResponseListener() {
            @Override
            public void onResponse(NewsDetails newsDetails) {


                mNewsId = newsDetails.id;
                mNewsUrl = newsDetails.url;

                Bundle bundle = new Bundle();
                bundle.putString("Vest", newsDetails.title);
                mFirebaseAnalytics.logEvent("android_komentar", bundle);

                newsIdList = MyMethodsClass.initNewsIdList(newsDetails.relatedNews);

//                getCommentVotes(newsDetails.commentsTop);

                updateList(newsDetails);

                setListeners(newsDetails);

                Log.d(TAG, "onResponse: " + newsDetails.title);

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.imageViewRefresh.setVisibility(View.VISIBLE);

                binding.nestedScrollView.setVisibility(View.GONE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
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

                initCommentsAdapter(newsDetails.commentsTop);
                initRelatedNewsAdapter(newsDetails.relatedNews);


                AdRequest adRequest3 = new AdRequest.Builder().build();
                binding.adView3.loadAd(adRequest3);

                binding.nestedScrollView.setVisibility(View.VISIBLE);
                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }


    private void initRelatedNewsAdapter(ArrayList<News> relatedNews) {

        binding.recyclerViewRelatedNews.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRelatedNews.setAdapter(new NewsDetailsRelatedNewsAdapter(relatedNews, this));

    }

    private void initCommentsAdapter(ArrayList<NewsComment> commentsTop) {
        binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewComments.setAdapter(new CommentsAdapter(this, commentsTop));
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
            handler.post(() -> checkVotedComments(comments, mVotes));

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
//                    handler.post(() -> adapter.commentLiked(id, vote));

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
//                    handler.post(() -> adapter.commentDisliked(id, vote));

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
    public void onNewsClicked(int newsId) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);

    }


}