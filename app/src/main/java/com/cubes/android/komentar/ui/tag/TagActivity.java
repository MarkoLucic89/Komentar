package com.cubes.android.komentar.ui.tag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.ActivityTagBinding;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;


public class TagActivity extends AppCompatActivity implements NewsListener {

    private ActivityTagBinding binding;
    private int tagId;
    private int nextPage = 1;
    private TagAdapter adapter;

    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;

        tagId = getIntent().getIntExtra("tag_id", -1);
        String tagTitle = getIntent().getStringExtra("tag_title");

        Bundle bundle = new Bundle();
        bundle.putString("Tag", tagTitle);
        FirebaseAnalytics.getInstance(this).logEvent("android_komentar", bundle);

        initRecyclerView();

        binding.progressBar.setVisibility(View.VISIBLE);

        loadNextPage();

        binding.textViewTitle.setText(tagTitle);

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewRefresh.setOnClickListener(view -> loadNextPage());

        binding.swipeRefreshLayout.setOnRefreshListener(this::refreshAdapter);


    }

    private void refreshAdapter() {

        nextPage = 1;

        dataRepository.getNewsForTag(tagId, nextPage, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                adapter.updateList(newsList, hasMorePages);

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    adapter.addRefresher();
                }

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void initRecyclerView() {
        adapter = new TagAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(TagActivity.this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadNextPage() {

        dataRepository.getNewsForTag(tagId, nextPage, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                if (nextPage == 1) {
                    adapter.updateList(newsList, hasMorePages);
                } else {
                    adapter.addNextPage(newsList, hasMorePages);
                }


                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    adapter.addRefresher();
                }

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void onNewsClicked(int newsId, int[] newsIdList) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", newsIdList);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            loadNextPage();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        dataRepository = null;
    }
}