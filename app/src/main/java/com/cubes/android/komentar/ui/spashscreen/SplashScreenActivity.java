package com.cubes.android.komentar.ui.spashscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cubes.android.komentar.databinding.ActivitySplashScreenBinding;
import com.cubes.android.komentar.ui.main.NewsListActivity;


public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() == null || getIntent().getExtras().getString("url") == null) {

            binding.getRoot().postDelayed(this::goToNewsListActivity, 2000);

            Log.d(TAG, "onCreate: getIntent().getExtras() == null");

        } else {

            Bundle bundle = getIntent().getExtras();

            String url = bundle.getString("url");

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

            Log.d(TAG, "onCreate: URL: " + url);

        }

    }


    private void goToNewsListActivity() {

        Intent intent = new Intent(this, NewsListActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}