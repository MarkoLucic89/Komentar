package com.cubes.android.komentar.ui.tag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.TagResponseModel;
import com.cubes.android.komentar.databinding.ActivityTagBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsActivity;
import com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;

import java.util.ArrayList;


public class TagActivity extends AppCompatActivity implements NewsListener {

    private ActivityTagBinding binding;
    private int tagId;
    private int nextPage = 1;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tagId = getIntent().getIntExtra("tag_id", -1);
        String tagTitle = getIntent().getStringExtra("tag_title");

        initRecyclerView();

        binding.progressBar.setVisibility(View.VISIBLE);

        loadNextPage();

        binding.textViewTitle.setText(tagTitle);

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewRefresh.setOnClickListener(view -> loadNextPage());


    }

    private void initRecyclerView() {
        adapter = new SearchAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(TagActivity.this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadNextPage() {

        DataRepository.getInstance().sendTagRequest(tagId, nextPage, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(TagResponseModel.TagDataResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                adapter.addNextPage(response);

                nextPage++;
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
            }
        });

    }

    @Override
    public void onNewsClicked(int newsId, String newsUrl, ArrayList<News> newsList) {

        int[] newsIdList = new int[newsList.size()];

        for (int i = 0; i < newsList.size(); i++) {
            newsIdList[i] = newsList.get(i).id;
        }

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_url", newsUrl);
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
    }
}