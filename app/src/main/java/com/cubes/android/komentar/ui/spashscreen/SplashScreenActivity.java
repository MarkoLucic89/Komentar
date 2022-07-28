package com.cubes.android.komentar.ui.spashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.data.source.remote.networking.response.categories_response.CategoriesResponseModel;
import com.cubes.android.komentar.databinding.ActivitySplashScreenBinding;
import com.cubes.android.komentar.data.source.remote.networking.NewsApi;
import com.cubes.android.komentar.ui.main.NewsListActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sendCategoriesRequest();
        
        binding.imageViewRefresh.setOnClickListener(view -> sendCategoriesRequest());

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendCategoriesRequest();
        }
    }

    private void sendCategoriesRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);


        NewsApi.getInstance().getNewsService().getCategories().enqueue(new Callback<CategoriesResponseModel>() {
            @Override
            public void onResponse(Call<CategoriesResponseModel> call, Response<CategoriesResponseModel> response) {

                DataContainer.categories = response.body().data;

                goToNewsListActivity();
            }

            @Override
            public void onFailure(Call<CategoriesResponseModel> call, Throwable t) {

                binding.imageViewRefresh.setVisibility(View.VISIBLE);

            }
        });
    }

    private void goToNewsListActivity() {

        Intent intent = new Intent(this, NewsListActivity.class);
        startActivity(intent);
        finish();

    }
}