package com.cubes.android.komentar.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cubes.android.komentar.databinding.ActivityNewsListBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.ui.main.home.HomeFragment;
import com.cubes.android.komentar.ui.main.latest.LatestNewsFragment;
import com.cubes.android.komentar.ui.main.search.SearchFragment;
import com.cubes.android.komentar.ui.main.videos.VideoFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class NewsListActivity extends AppCompatActivity {

    private ActivityNewsListBinding binding;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeFragment = HomeFragment.newInstance();

        replaceFragment(homeFragment);

        setListeners();

        addStickyAd();

    }

    @Override
    public void onBackPressed() {
        if (homeFragment.onBackPressed()) {
            finish();
        }

    }

    private void addStickyAd() {

        binding.imageViewClose.setVisibility(View.GONE);
        binding.adViewSticky.setVisibility(View.GONE);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adViewSticky.loadAd(adRequest);
        binding.adViewSticky.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                binding.imageViewClose.setVisibility(View.VISIBLE);
                binding.adViewSticky.setVisibility(View.VISIBLE);
            }
        });


        binding.imageViewClose.setOnClickListener(view -> {
            binding.imageViewClose.setVisibility(View.GONE);
            binding.adViewSticky.setVisibility(View.GONE);
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void setListeners() {


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menuHome:
                    homeFragment = HomeFragment.newInstance();
                    replaceFragment(homeFragment);
                    return true;
                case R.id.menuLatest:
                    replaceFragment(LatestNewsFragment.newInstance());
                    return true;
                case R.id.menuVideo:
                    replaceFragment(VideoFragment.newInstance());
                    return true;
                case R.id.menuSearch:
                    replaceFragment(SearchFragment.newInstance());
                    return true;
                default:
                    return false;
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeFragment = null;
    }
}