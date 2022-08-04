package com.cubes.android.komentar.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.cubes.android.komentar.databinding.ActivityNewsListBinding;

import android.os.Bundle;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.ui.main.home.HomeFragment;
import com.cubes.android.komentar.ui.main.latest.LatestNewsFragment;
import com.cubes.android.komentar.ui.main.search.SearchFragment;
import com.cubes.android.komentar.ui.main.videos.VideoFragment;

public class NewsListActivity extends AppCompatActivity {

    private ActivityNewsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(HomeFragment.newInstance());
        setListeners();

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
                    replaceFragment(HomeFragment.newInstance());
                    break;
                case R.id.menuLatest:
                    replaceFragment(LatestNewsFragment.newInstance());
                    break;
                case R.id.menuVideo:
                    replaceFragment(VideoFragment.newInstance());
                    break;
                case R.id.menuSearch:
                    replaceFragment(SearchFragment.newInstance());
                    break;
            }

            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}