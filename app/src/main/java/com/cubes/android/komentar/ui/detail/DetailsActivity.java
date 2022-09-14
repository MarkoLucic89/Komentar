package com.cubes.android.komentar.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.source.local.database.dao.NewsBookmarksDao;
import com.cubes.android.komentar.databinding.ActivityDetailsBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsActivity extends AppCompatActivity implements NewsDetailsFragment.DetailsListener {

    private ActivityDetailsBinding binding;
    private int mNewsId;
    private String mNewsUrl;

    private NewsBookmarksDao bookmarksDao;

    private News mBookmarkNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;

        bookmarksDao = appContainer.room.bookmarksDao();

        mNewsId = getIntent().getIntExtra("news_id", -1);
        int[] mNewsIdList = getIntent().getIntArrayExtra("news_id_list");

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

        binding.imageViewBookmark.setOnClickListener(view -> {

            if (mBookmarkNews != null) {

                //Room
                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                service.execute(() -> {

                    //doInBackgroundThread
                    if (mBookmarkNews.isInBookmarks) {
                        bookmarksDao.delete(mBookmarkNews);
                    } else {
                        bookmarksDao.insert(mBookmarkNews);
                    }

                    //onPostExecute
                    handler.post(() -> {

                        if (mBookmarkNews.isInBookmarks) {
                            Toast.makeText(this, "Vest je uspešno uklonjena", Toast.LENGTH_SHORT).show();
                            binding.imageViewBookmark.setImageResource(R.drawable.ic_bookmark_border);
                        } else {
                            Toast.makeText(this, "Vest je uspešno sačuvana u arhivu", Toast.LENGTH_SHORT).show();
                            binding.imageViewBookmark.setImageResource(R.drawable.ic_bookmark);
                        }

                        mBookmarkNews.isInBookmarks = !mBookmarkNews.isInBookmarks;

                    });

                });

                service.shutdown();

            }

        });

    }


    @Override
    public void onDetailsResponseListener(int newsId, String newsUrl, News tempNews) {

        mNewsId = newsId;
        mNewsUrl = newsUrl;
        mBookmarkNews = tempNews;

        if (mBookmarkNews.isInBookmarks) {
            binding.imageViewBookmark.setImageResource(R.drawable.ic_bookmark);
        } else {
            binding.imageViewBookmark.setImageResource(R.drawable.ic_bookmark_border);
        }
    }
}