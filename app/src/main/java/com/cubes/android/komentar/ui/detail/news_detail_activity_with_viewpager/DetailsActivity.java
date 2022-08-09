package com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.ActivityDetailsBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private int newsId;
    private String newsUrl;
    private int[] newsIdList;

    private DetailsListener detailsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newsId = getIntent().getIntExtra("news_id", -1);
        newsUrl = getIntent().getStringExtra("news_url");
        newsIdList = getIntent().getIntArrayExtra("news_id_list");

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, newsIdList);
        binding.viewPager.setAdapter(adapter);

        for (int i = 0; i < newsIdList.length; i++) {
            if (newsId == newsIdList[i]) {
                binding.viewPager.setCurrentItem(i);
                break;
            }
        }

        setClickListeners();

    }

    private void setClickListeners() {

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewShare.setOnClickListener(view -> {
//            detailsListener.onShareClickListener();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        binding.imageViewMessages.setOnClickListener(view -> {
//            detailsListener.onMessagesClickListener();
            Intent intent = new Intent(this, CommentsActivity.class);
            intent.putExtra("news_id", newsId);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public interface DetailsListener{

        void onShareClickListener();

        void onMessagesClickListener();

    }
}