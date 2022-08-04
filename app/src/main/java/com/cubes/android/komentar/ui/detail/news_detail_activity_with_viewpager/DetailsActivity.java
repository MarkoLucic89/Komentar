package com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private int newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newsId = getIntent().getIntExtra("news_id", -1);

        sendNewsDetailsRequest();


    }

    private void sendNewsDetailsRequest() {

        DataRepository.getInstance().sendNewsDetailsRequest(newsId, new DataRepository.DetailResponseListener() {

            @Override
            public void onResponse(NewsDetailsResponseModel.NewsDetailsDataResponseModel response) {

                ViewPagerAdapter adapter = new ViewPagerAdapter(DetailsActivity.this, response.category_news);
                binding.viewPager.setAdapter(adapter);

                for (int i = 0; i < response.category_news.size(); i++) {
                    binding.viewPager.setCurrentItem(i);
                    break;
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}