package com.cubes.android.komentar.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cubes.android.komentar.databinding.ActivityNewsListBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.ui.main.bookmarks.BookmarkDialogFragment;
import com.cubes.android.komentar.ui.main.bookmarks.BookmarksFragment;
import com.cubes.android.komentar.ui.main.home.HomeFragment;
import com.cubes.android.komentar.ui.main.latest.LatestNewsFragment;
import com.cubes.android.komentar.ui.main.search.SearchFragment;
import com.cubes.android.komentar.ui.main.videos.VideoFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class NewsListActivity extends AppCompatActivity implements BookmarkDialogFragment.DeleteAllBookmarksListener {

    private boolean isFirstTimeBackClicked = true;

    private ActivityNewsListBinding binding;
    private HomeFragment homeFragment;
    private BookmarksFragment bookmarkFragment;

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

            if (isFirstTimeBackClicked) {

                isFirstTimeBackClicked = false;
                Toast.makeText(this, "Pritisnite ponovo da biste izašli iz aplikacije", Toast.LENGTH_SHORT).show();

                binding.getRoot().postDelayed(() -> isFirstTimeBackClicked = true, 3);

            } else {

                finish();

            }
        }

    }

    private void addStickyAd() {

        binding.adViewSticky.setVisibility(View.GONE);
        binding.shimmerLayout.setVisibility(View.VISIBLE);
        binding.shimmerLayout.startLayoutAnimation();

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adViewSticky.loadAd(adRequest);
        binding.adViewSticky.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                binding.adViewSticky.setVisibility(View.VISIBLE);
                binding.shimmerLayout.stopShimmer();
                binding.shimmerLayout.setVisibility(View.GONE);
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
                case R.id.menuBookmarks:
                    bookmarkFragment = BookmarksFragment.newInstance();
                    replaceFragment(bookmarkFragment);
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        homeFragment = null;
//        bookmarkFragment = null;
//    }

    @Override
    public void onDeleteALlBookmarks() {
        bookmarkFragment.deleteAllFavorites();
    }
}