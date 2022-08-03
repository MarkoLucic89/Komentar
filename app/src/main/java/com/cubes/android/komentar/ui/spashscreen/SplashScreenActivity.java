package com.cubes.android.komentar.ui.spashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoriesResponseModel;
import com.cubes.android.komentar.databinding.ActivitySplashScreenBinding;
import com.cubes.android.komentar.data.source.remote.networking.NewsApi;
import com.cubes.android.komentar.ui.main.NewsListActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        goToNewsListActivity();

        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
                 Context context = SplashScreenActivity.this;
            }
        });

    }


    private void goToNewsListActivity() {

        Intent intent = new Intent(this, NewsListActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}