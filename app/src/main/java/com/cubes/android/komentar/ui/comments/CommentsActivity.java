package com.cubes.android.komentar.ui.comments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.NewsComment;
import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.cubes.android.komentar.data.source.local.CommentPrefs;
import com.cubes.android.komentar.databinding.ActivityCommentsBinding;
import com.cubes.android.komentar.ui.post_comment.PostCommentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommentsActivity extends AppCompatActivity {

    private ActivityCommentsBinding binding;
    private int news_id;
    private CommentsAdapter adapter;
    private List<NewsCommentVote> mVotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        news_id = getIntent().getIntExtra("news_id", -1);

        binding.imageViewBack.setOnClickListener(view -> finish());
        binding.imageViewRefresh.setOnClickListener(view -> getComments());

        initRecyclerView();

        binding.recyclerViewComments.setVisibility(View.GONE);

        getComments();

        binding.swipeRefreshLayout.setOnRefreshListener(() -> refreshList());

    }

    private void refreshList() {

        DataRepository.getInstance().getCommentsRequest(news_id, new DataRepository.CommentsResponseListener() {

            @Override
            public void onResponse(ArrayList<NewsComment> comments) {

                if (comments.isEmpty()) {
                    binding.textView.setVisibility(View.VISIBLE);
                } else {
                    binding.textView.setVisibility(View.GONE);

                    getCommentVotes(comments);
//                    checkVotedComments(comments, mVotes);
                }

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.textView.setVisibility(View.GONE);
                binding.recyclerViewComments.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void getComments() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().getCommentsRequest(news_id, new DataRepository.CommentsResponseListener() {

            @Override
            public void onResponse(ArrayList<NewsComment> comments) {

                binding.textView.setVisibility(View.GONE);
                binding.recyclerViewComments.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);

                if (comments.isEmpty()) {
                    binding.textView.setVisibility(View.VISIBLE);
                } else {
                    binding.textView.setVisibility(View.GONE);
                    getCommentVotes(comments);
                }

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                binding.textView.setVisibility(View.GONE);
                binding.recyclerViewComments.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void getCommentVotes(ArrayList<NewsComment> comments) {

        //ROOM

//        ExecutorService service = Executors.newSingleThreadExecutor();
//        service.execute(() -> {
//
//            //doInBackgroundThread
//            List<NewsCommentVote> votes = NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().getNewsCommentVotes();
//
//            //onPostExecute
//            runOnUiThread(() -> checkVotedComments(comments, votes));
//
//        });
//
//        service.shutdown();


        //SHARED PREFS

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            //doInBackgroundThread
            ArrayList votes = (ArrayList) CommentPrefs.readListFromPref(this);

            if (votes != null) {
                mVotes.addAll(votes);
            }
            //onPostExecute
            runOnUiThread(() -> checkVotedComments(comments, mVotes));

        });

        service.shutdown();

    }

    private void checkVotedComments(ArrayList<NewsComment> comments, List<NewsCommentVote> votes) {

        for (NewsCommentVote vote : votes) {

            for (NewsComment comment : comments) {

                if (vote.id.equals(comment.id)) {
                    comment.newsCommentVote = vote;
                }

                if (comment.children != null) {
                    checkChildrenVotes(comment.children, vote);
                }

            }
        }

        adapter.updateList(comments);

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

    private void initRecyclerView() {

        adapter = new CommentsAdapter(new CommentsAdapter.CommentsListener() {
            @Override
            public void onLikeListener(int id, boolean vote) {
                likeComment(id, vote);
            }

            @Override
            public void onDislikeListener(int id, boolean vote) {
                dislikeComment(id, vote);
            }

            @Override
            public void goOnPostCommentActivity(int newsId, int reply_id) {

                Intent intent = new Intent(CommentsActivity.this, PostCommentActivity.class);
                intent.putExtra("news", newsId);
                intent.putExtra("reply_id", reply_id);
                startActivity(intent);

            }
        });

        binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewComments.setAdapter(adapter);
    }

    private void likeComment(int id, boolean vote) {

        DataRepository.getInstance().likeCommentRequest(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                //ROOM

//                ExecutorService service = Executors.newSingleThreadExecutor();
//                service.execute(() -> {
//
//                    //doInBackgroundThread
//                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
//                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);
//
//                    //onPostExecute
//                    runOnUiThread(() -> adapter.commentLiked(id, vote));
//
//                });
//
//                service.shutdown();

                //SHARED PREFS

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
                    mVotes.add(newsCommentVote);
                    CommentPrefs.writeListInPref(CommentsActivity.this, mVotes);

                    //onPostExecute
                    runOnUiThread(() -> adapter.commentLiked(id, vote));

                });

                service.shutdown();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(CommentsActivity.this, "Komentar nije izglasan, došlo je do greške.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dislikeComment(int id, boolean vote) {

        DataRepository.getInstance().dislikeCommentRequest(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                //ROOM

//                ExecutorService service = Executors.newSingleThreadExecutor();
//                service.execute(() -> {
//
//                    //doInBackgroundThread
//                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), false);
//                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);
//
//                    //onPostExecute
//                    runOnUiThread(() -> adapter.commentDisliked(id, vote));
//
//                });
//
//                service.shutdown();

                //SHARED PREFS

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
                    mVotes.add(newsCommentVote);
                    CommentPrefs.writeListInPref(CommentsActivity.this, mVotes);

                    //onPostExecute
                    runOnUiThread(() -> adapter.commentDisliked(id, vote));

                });

                service.shutdown();
            }


            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(CommentsActivity.this, "Komentar nije izglasan, došlo je do greške.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }


}