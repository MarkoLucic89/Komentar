package com.cubes.android.komentar.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cubes.android.komentar.databinding.ActivityNewsListBinding;

import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(HomeFragment.newInstance());
        setListeners();

        addStickyAd();

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


//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//
//            switch (item.getItemId()) {
//                case R.id.menuHome:
//                    replaceFragment(HomeFragment.newInstance());
//                    break;
//                case R.id.menuLatest:
//                    replaceFragment(LatestNewsFragment.newInstance());
//                    break;
//                case R.id.menuVideo:
//                    replaceFragment(VideoFragment.newInstance());
//                    break;
//                case R.id.menuSearch:
//                    replaceFragment(SearchFragment.newInstance());
//                    break;
//            }
//
//            return true;
//        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.menuHome:
                    fragment = HomeFragment.newInstance();
                    break;
                case R.id.menuLatest:
                    fragment = LatestNewsFragment.newInstance();
                    break;
                case R.id.menuVideo:
                    fragment = VideoFragment.newInstance();
                    break;
                case R.id.menuSearch:
                    fragment = SearchFragment.newInstance();
                    break;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();

            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        binding = null;
    }
}