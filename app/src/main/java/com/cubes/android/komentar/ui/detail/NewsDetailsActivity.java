package com.cubes.android.komentar.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.data.source.local.database.NewsDatabase;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.ActivityNewsDetailsBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.post_comment.PostCommentActivity;
import com.cubes.android.komentar.ui.tag.TagActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
NE KORISTI SE VISE
 */

public class NewsDetailsActivity extends AppCompatActivity implements
        CommentsAdapter.CommentsListener,
        NewsDetailsTagsAdapter.TagListener,
        NewsListener {

    private ActivityNewsDetailsBinding binding;
    private int newsId;
    private NewsDetailsResponseModel.NewsDetailsDataResponseModel data;
    private NewsDetailsAdapter adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

        sendNewsDetailsRequest();

        setListeners();

    }

    private void setListeners() {
        binding.imageViewRefresh.setOnClickListener(view -> sendNewsDetailsRequest());

        binding.imageViewMessages.setOnClickListener(view -> goToCommentsActivity(newsId));

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewShare.setOnClickListener(view -> {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, data.url);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
    }


    private void sendNewsDetailsRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        newsId = getIntent().getIntExtra("news_id", -1);

        DataRepository.getInstance().sendNewsDetailsRequest(newsId, new DataRepository.DetailResponseListener() {

            @Override
            public void onResponse(NewsDetailsResponseModel.NewsDetailsDataResponseModel response) {
                data = response;

                binding.progressBar.setVisibility(View.GONE);

                adapter.updateList(response);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initRecyclerView() {
        adapter = new NewsDetailsAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(NewsDetailsActivity.this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendNewsDetailsRequest();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onLikeListener(int id, boolean vote) {

        DataRepository.getInstance().likeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), vote);
                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);

                    //onPostExecute
//                    runOnUiThread(() -> adapter.commentLiked(id, vote));

                });

                service.shutdown();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(NewsDetailsActivity.this, "Komentar nije izglasan, došlo je do greške.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDislikeListener(int id, boolean vote) {

        DataRepository.getInstance().dislikeComment(id, vote, new DataRepository.CommentsVoteListener() {
            @Override
            public void onResponse(NewsCommentVote response) {

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {

                    //doInBackgroundThread
                    NewsCommentVote newsCommentVote = new NewsCommentVote(String.valueOf(id), false);
                    NewsDatabase.getInstance(binding.getRoot().getContext()).voteDao().insert(newsCommentVote);

                    //onPostExecute
                    runOnUiThread(() -> adapter.commentDisliked(id, vote));

                });

                service.shutdown();
            }


            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(NewsDetailsActivity.this, "Komentar nije izglasan, došlo je do greške.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void goOnPostCommentActivity(int newsId, int reply_id) {
        Intent intent = new Intent(this, PostCommentActivity.class);
        intent.putExtra("news", newsId);
        intent.putExtra("reply_id", reply_id);
        startActivity(intent);

    }

    @Override
    public void goToCommentsActivity(int newsId) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("news_id", newsId);
        startActivity(intent);
    }

    @Override
    public void onTagClicked(int tagId, String tagTitle) {
        Intent intent = new Intent(this, TagActivity.class);
        intent.putExtra("tag_id", tagId);
        intent.putExtra("tag_title", tagTitle);
        startActivity(intent);
    }

}