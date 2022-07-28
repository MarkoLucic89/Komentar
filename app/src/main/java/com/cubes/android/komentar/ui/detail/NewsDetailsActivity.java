package com.cubes.android.komentar.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.news_details_response.NewsDetailsDataResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.news_details_response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.ActivityNewsDetailsBinding;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class NewsDetailsActivity extends AppCompatActivity {

    private ActivityNewsDetailsBinding binding;
    private int newsId;
    private NewsDetailsDataResponseModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sendNewsDetailsRequest();

        setListeners();

    }

    private void setListeners() {
        binding.imageViewRefresh.setOnClickListener(view -> sendNewsDetailsRequest());
        binding.imageViewMessages.setOnClickListener(view -> MyMethodsClass.goToCommentsActivity(view, newsId));
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
            public void onResponse(NewsDetailsResponseModel response) {
                binding.progressBar.setVisibility(View.GONE);

                data = response.data;

                initRecyclerView(data);
            }

            @Override
            public void onFailure(Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initRecyclerView(NewsDetailsDataResponseModel newsDetails) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(NewsDetailsActivity.this));
        binding.recyclerView.setAdapter(new NewsDetailsAdapter(newsDetails));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendNewsDetailsRequest();
        }

    }

}