package com.cubes.android.komentar.ui.spashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cubes.android.komentar.databinding.ActivitySplashScreenBinding;
import com.cubes.android.komentar.ui.main.NewsListActivity;


public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNewsListActivity();
            }
        }, 1000);
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