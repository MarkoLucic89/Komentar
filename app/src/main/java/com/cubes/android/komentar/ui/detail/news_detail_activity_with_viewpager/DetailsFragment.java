package com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager;

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
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.data.source.local.database.NewsDatabase;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.FragmentDetailsBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsActivity;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsTagsAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.post_comment.PostCommentActivity;
import com.cubes.android.komentar.ui.tag.TagActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsFragment extends Fragment implements
        CommentsAdapter.CommentsListener,
        NewsDetailsTagsAdapter.TagListener,
        NewsListener {

    private FragmentDetailsBinding binding;

    private static final String NEWS_ID = "news_id";
    private static final String NEWS_URL = "news_url";
    private static final String NEWS_ID_LIST = "news_id_list";

    private int mNewsId;
    private String mNewsUrl;
    private int[] mNewsIdList;

    private NewsDetailsResponseModel.NewsDetailsDataResponseModel data;
    private NewsDetailsAdapter adapter;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(int newsId, int[] newsIdList) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(NEWS_ID, newsId);
        args.putIntArray(NEWS_ID_LIST, newsIdList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getInt(NEWS_ID);
            mNewsIdList = getArguments().getIntArray(NEWS_ID_LIST);
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

        sendNewsDetailsRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> sendNewsDetailsRequest());

    }


    private void initRecyclerView() {
        adapter = new NewsDetailsAdapter(this, this, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void sendNewsDetailsRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().sendNewsDetailsRequest(mNewsId, new DataRepository.DetailResponseListener() {
            @Override
            public void onResponse(NewsDetailsResponseModel.NewsDetailsDataResponseModel response) {

                mNewsUrl = response.url;

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                adapter.updateList(response);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });
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
    public void onNewsClicked(int newsId) {
        Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
        intent.putExtra("news_id", newsId);
        startActivity(intent);
    }

//    @Override
//    public void onNewsClicked(int newsId, String newsUrl, ArrayList<News> news) {
//        Intent intent = new Intent(getContext(), DetailsFragment.class);
//        intent.putExtra("news_id", mNewsId);
//        intent.putExtra("news_url", mNewsUrl);
//        intent.putExtra("news_id_list", mNewsIdList);
//        startActivity(intent);
//    }


    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendNewsDetailsRequest();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}