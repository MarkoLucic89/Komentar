package com.cubes.android.komentar.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.DetailsListener {

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

//        addStickyAd();

        for (int i = 0; i < mNewsIdList.length; i++) {
            if (mNewsId == mNewsIdList[i]) {
                binding.viewPager.setCurrentItem(i, false);
                break;
            }
        }

        setClickListeners();

        MyMethodsClass.startArrowFadeAnimation(binding.imageViewArrowLeft);
        MyMethodsClass.startArrowFadeAnimation(binding.imageViewArrowRight);

//        reduceDragSensitivity(1);
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

//    private void addStickyAd() {
//
//        binding.adViewSticky.setVisibility(View.GONE);
//        binding.shimmerLayout.setVisibility(View.VISIBLE);
//        binding.shimmerLayout.startLayoutAnimation();
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adViewSticky.loadAd(adRequest);
//        binding.adViewSticky.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//
//                binding.adViewSticky.setVisibility(View.VISIBLE);
//                binding.shimmerLayout.stopShimmer();
//                binding.shimmerLayout.setVisibility(View.GONE);
//            }
//        });
//
//
//        binding.imageViewClose.setOnClickListener(view -> binding.frameLayoutSticky.setVisibility(View.GONE));
//    }

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

    private void reduceDragSensitivity(int sensitivity) {
        try {
            Field ff = ViewPager2.class.getDeclaredField("mRecyclerView");
            ff.setAccessible(true);
            RecyclerView recyclerView = (RecyclerView) ff.get(binding.viewPager);
            Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop");
            touchSlopField.setAccessible(true);
            int touchSlop = (int) touchSlopField.get(recyclerView);
            touchSlopField.set(recyclerView, touchSlop * sensitivity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}