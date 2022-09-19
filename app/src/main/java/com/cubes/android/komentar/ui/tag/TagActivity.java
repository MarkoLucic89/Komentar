package com.cubes.android.komentar.ui.tag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.local.database.dao.NewsBookmarksDao;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.ActivityTagBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TagActivity extends AppCompatActivity implements NewsListener {

    private ActivityTagBinding binding;
    private int tagId;
    private int nextPage = 1;
    private TagAdapter adapter;

    private DataRepository dataRepository;

    private NewsBookmarksDao bookmarksDao;

    private ArrayList<News> bookmarks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;

        bookmarksDao = appContainer.room.bookmarksDao();

        tagId = getIntent().getIntExtra("tag_id", -1);
        String tagTitle = getIntent().getStringExtra("tag_title");

        Bundle bundle = new Bundle();
        bundle.putString("Tag", tagTitle);
        FirebaseAnalytics.getInstance(this).logEvent("android_komentar", bundle);

        initRecyclerView();

        binding.swipeRefreshLayout.setRefreshing(true);

        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            bookmarks.clear();
            bookmarks.addAll(bookmarksDao.getBookmarkNews());

            //onPostExecute
            handler.post(this::initList);

        });

        service.shutdown();


        binding.textViewTitle.setText(tagTitle);

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewRefresh.setOnClickListener(view -> {

            binding.swipeRefreshLayout.setRefreshing(true);

            loadNextPage();

        });

        binding.swipeRefreshLayout.setOnRefreshListener(this::initList);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            loadNextPage();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.closeAllMenus();
    }

    private void initList() {

        nextPage = 1;

        dataRepository.getNewsForTag(tagId, nextPage, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                MyMethodsClass.checkBookmarks(newsList, bookmarks);

                adapter.initList(newsList, hasMorePages);

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                } else {
                    adapter.addRefresher();
                }

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void initRecyclerView() {
        adapter = new TagAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(TagActivity.this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadNextPage() {

        dataRepository.getNewsForTag(tagId, nextPage, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                MyMethodsClass.checkBookmarks(newsList, bookmarks);

                if (nextPage == 1) {
                    adapter.initList(newsList, hasMorePages);
                } else {
                    adapter.addNextPage(newsList, hasMorePages);
                }

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                } else {
                    adapter.addRefresher();
                }

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void refreshPage() {

        dataRepository.getNewsForTag(tagId, nextPage, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                MyMethodsClass.checkBookmarks(newsList, bookmarks);

                if (nextPage == 1) {
                    adapter.initList(newsList, hasMorePages);
                } else {
                    adapter.refreshList(newsList, hasMorePages);
                }

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                } else {
                    adapter.addRefresher();
                }

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void onNewsClicked(int newsId, int[] newsIdList) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", newsIdList);
        startActivity(intent);

    }


    @Override
    public void onNewsMenuCommentsClicked(int newsId) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("news_id", newsId);
        startActivity(intent);
    }

    @Override
    public void onNewsMenuShareClicked(String url) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onNewsMenuFavoritesClicked(News news) {

        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            if (news.isInBookmarks) {
                bookmarksDao.delete(news);
            } else {
                bookmarksDao.insert(news);
            }

            //onPostExecute
            if (news.isInBookmarks) {
                handler.post(() -> Toast.makeText(this, "Vest je uspešno uklonjena iz arhive", Toast.LENGTH_SHORT).show());
                news.isInBookmarks = false;
            } else {
                handler.post(() -> Toast.makeText(this, "Vest je uspešno sačuvana u arhivu", Toast.LENGTH_SHORT).show());
                news.isInBookmarks = true;
            }

        });

        service.shutdown();
    }


    @Override
    public void closeOtherMenus() {
        adapter.closeAllMenus();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding = null;
//        dataRepository = null;
//    }
}