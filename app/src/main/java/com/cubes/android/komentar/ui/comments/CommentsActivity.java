package com.cubes.android.komentar.ui.comments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.data.source.local.database.NewsDatabase;
import com.cubes.android.komentar.databinding.ActivityCommentsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommentsActivity extends AppCompatActivity {

    private ActivityCommentsBinding binding;
    private int news_id;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        news_id = getIntent().getIntExtra("news_id", -1);

        binding.imageViewBack.setOnClickListener(view -> finish());

        DataRepository.getInstance().getComments(news_id, new DataRepository.CommentsResponseListener() {

            @Override
            public void onResponse(ArrayList<NewsComment> comments) {
                if (comments.isEmpty()) {
                    binding.textView.setVisibility(View.VISIBLE);
                } else {
                    getCommentVotes(comments);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void getCommentVotes(ArrayList<NewsComment> comments) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            //onPreExecute
            runOnUiThread(() -> {

            });

            //doInBackgroundThread
            List<NewsCommentVote> votes = NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().getNewsCommentVotes();

            //onPostExecute
            runOnUiThread(() -> {

                for (NewsCommentVote vote : votes) {
                    for (NewsComment comment : comments) {
                        if (vote.id.equals(comment.id)) {
                            comment.newsCommentVote = vote;
                        }
                    }
                }

                initRecyclerView(comments);
            });

        });



    }

    private void initRecyclerView(ArrayList<NewsComment> comments) {
        adapter = new CommentsAdapter(comments, new CommentsListener() {
            @Override
            public void onLikeListener(int id, boolean vote) {
                likeComment(id, vote);
            }

            @Override
            public void onDislikeListener(int id, boolean vote) {
                dislikeComment(id, vote);
            }
        });
        binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewComments.setAdapter(adapter);
    }

    private void likeComment(int id, boolean vote) {

        DataRepository.getInstance().likeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {

                    //onPreExecute
                    runOnUiThread(() -> {

                    });

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);

                    //onPostExecute
                    runOnUiThread(() -> adapter.commentLiked(id, vote));

                });

                service.shutdown();

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void dislikeComment(int id, boolean vote) {

        DataRepository.getInstance().dislikeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {

                    //onPreExecute
                    runOnUiThread(() -> {

                    });

                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), false);
                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);

                    //onPostExecute
                    runOnUiThread(() -> adapter.commentDisliked(id, vote));

                });

                service.shutdown();
            }


            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }


}