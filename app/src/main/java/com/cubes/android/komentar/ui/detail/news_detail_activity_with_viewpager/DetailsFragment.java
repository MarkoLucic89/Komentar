package com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager;

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
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.data.source.local.CommentPrefs;
import com.cubes.android.komentar.data.source.local.database.NewsDatabase;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.FragmentDetailsBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsTagsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.post_comment.PostCommentActivity;
import com.cubes.android.komentar.ui.tag.TagActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsFragment extends Fragment implements
        CommentsAdapter.CommentsListener,
        NewsDetailsTagsAdapter.TagListener,
        NewsListener {

    private FragmentDetailsBinding binding;

    private static final String NEWS_ID = "news_id";

    private int mNewsId;
    private String mNewsUrl;

    private NewsDetailsAdapter adapter;

    private DetailsListener listener;

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

        initRecyclerView();
        getNewsDetails();
        binding.imageViewRefresh.setOnClickListener(view1 -> getNewsDetails());

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
    }


    private void initRecyclerView() {
        adapter = new NewsDetailsAdapter(this, this, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void getNewsDetails() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().sendNewsDetailsRequest(mNewsId, new DataRepository.DetailResponseListener() {
            @Override
            public void onResponse(NewsDetailsResponseModel.NewsDetailsDataResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                mNewsId = response.id;
                mNewsUrl = response.url;

                listener.onDetailsResponseListener(mNewsId, mNewsUrl);

                getCommentVotes(response.comments_top_n);

                adapter.updateList(response);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
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
            ArrayList votes = (ArrayList) CommentPrefs.readListFromPref(getActivity());

            //onPostExecute
            handler.post(() -> checkVotedComments(comments, votes));

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
    public void onLikeListener(CommentsAdapter adapter, int id, boolean vote) {

        DataRepository.getInstance().likeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);

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
    public void onDislikeListener(CommentsAdapter adapter, int id, boolean vote) {

        DataRepository.getInstance().dislikeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), false);
                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);

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
    public void onNewsClicked(int newsId, String newsUrl, int[] newsIdList) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_url", newsUrl);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);
        getActivity().finish();

    }
}