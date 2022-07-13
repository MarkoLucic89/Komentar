package com.cubes.android.komentar.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.android.komentar.data.model.response.news_details_response.NewsDetailsDataResponseModel;
import com.cubes.android.komentar.data.model.response.news_details_response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.ActivityNewsDetailsBinding;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        NewsApi.getInstance().getNewsService().getNewsDetails(newsId).enqueue(new Callback<NewsDetailsResponseModel>() {
            @Override
            public void onResponse(Call<NewsDetailsResponseModel> call, Response<NewsDetailsResponseModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                data = response.body().data;

                initRecyclerView(data);

            }

            @Override
            public void onFailure(Call<NewsDetailsResponseModel> call, Throwable t) {

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