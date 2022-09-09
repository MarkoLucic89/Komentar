package com.cubes.android.komentar.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.cubes.android.komentar.databinding.ActivityDetailsBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

public class DetailsActivity extends AppCompatActivity implements NewsDetailsFragment.DetailsListener {

    private ActivityDetailsBinding binding;
    private int mNewsId;
    private String mNewsUrl;
    private int[] mNewsIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mNewsId = getIntent().getIntExtra("news_id", -1);
        mNewsIdList = getIntent().getIntArrayExtra("news_id_list");

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, mNewsIdList);
        binding.viewPager.setAdapter(adapter);

//        binding.viewPager.setOffscreenPageLimit(1);

        for (int i = 0; i < mNewsIdList.length; i++) {
            if (mNewsId == mNewsIdList[i]) {
                binding.viewPager.setCurrentItem(i);
                break;
            }
        }

        setClickListeners();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                    MyMethodsClass.startArrowFadeAnimation(binding.imageViewArrowLeft);

                    MyMethodsClass.startArrowFadeAnimation(binding.imageViewArrowRight);

            }
        });
    }

    private void setClickListeners() {

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewShare.setOnClickListener(view -> {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mNewsUrl);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        binding.imageViewMessages.setOnClickListener(view -> {

            Intent intent = new Intent(this, CommentsActivity.class);
            intent.putExtra("news_id", mNewsId);
            startActivity(intent);
        });

    }


    @Override
    public void onDetailsResponseListener(int newsId, String newsUrl) {
        mNewsId = newsId;
        mNewsUrl = newsUrl;
    }
}