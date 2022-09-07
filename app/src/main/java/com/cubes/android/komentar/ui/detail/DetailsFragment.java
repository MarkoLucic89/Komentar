package com.cubes.android.komentar.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.NewsComment;
import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.cubes.android.komentar.data.model.domain.NewsDetails;
import com.cubes.android.komentar.data.source.local.SharedPrefs;
import com.cubes.android.komentar.databinding.FragmentDetailsBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.post_comment.PostCommentActivity;
import com.cubes.android.komentar.ui.tag.TagActivity;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
        TRENUTNO SE NE KORISTI!!!
 */

public class DetailsFragment extends Fragment implements
        CommentsAdapter.CommentsListener,
        NewsDetailsTagsAdapter.TagListener,
        NewsListener {

    private FragmentDetailsBinding binding;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String NEWS_ID = "news_id";

    private int mNewsId;
    private String mNewsUrl;

    private int[] newsIdList;

    private NewsDetailsAdapter adapter;

    private DetailsListener listener;

    private ArrayList<NewsCommentVote> mVotes = new ArrayList<>();

    private DataRepository dataRepository;

    public interface DetailsListener {

        void onDetailsResponseListener(int newsId, String newsUrl);

    }


    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(int newsId) {
        DetailsFragment fragment = new DetailsFragment();
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
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.VISIBLE);

        initRecyclerView();

        getNewsDetails();

        binding.imageViewRefresh.setOnClickListener(view1 -> {
            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
            getNewsDetails();
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> refreshNewsDetails());
    }

    private void refreshNewsDetails() {

        dataRepository.getNewsDetails(mNewsId, new DataRepository.DetailResponseListener() {
            @Override
            public void onResponse(NewsDetails newsDetails) {

                mNewsId = newsDetails.id;
                mNewsUrl = newsDetails.url;

                listener.onDetailsResponseListener(mNewsId, mNewsUrl);

                getCommentVotes(newsDetails.commentsTop);

                adapter.updateList(newsDetails);

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            getNewsDetails();
        } else {
            listener.onDetailsResponseListener(mNewsId, mNewsUrl);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.listener = null;
        binding = null;
        dataRepository = null;
    }


    private void initRecyclerView() {
        adapter = new NewsDetailsAdapter(this, this, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void getNewsDetails() {

        binding.imageViewRefresh.setVisibility(View.GONE);

        dataRepository.getNewsDetails(mNewsId, new DataRepository.DetailResponseListener() {
            @Override
            public void onResponse(NewsDetails newsDetails) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);

                mNewsId = newsDetails.id;
                mNewsUrl = newsDetails.url;

                newsIdList = MyMethodsClass.initNewsIdList(newsDetails.relatedNews);

                listener.onDetailsResponseListener(mNewsId, mNewsUrl);

                Bundle bundle = new Bundle();
                bundle.putString("news_title", newsDetails.title);
                mFirebaseAnalytics.logEvent("news", bundle);


                getCommentVotes(newsDetails.commentsTop);

                adapter.updateList(newsDetails);

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
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
            ArrayList votes = (ArrayList) SharedPrefs.readListFromPref(getActivity());

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
                    handler.post(() -> adapter.commentLiked(id, vote));

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
                    handler.post(() -> adapter.commentDisliked(id, vote));

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