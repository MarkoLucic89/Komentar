package com.cubes.android.komentar.ui.main.home.home_pager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.source.local.database.dao.NewsBookmarksDao;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.data.model.domain.HomePageData;
import com.cubes.android.komentar.databinding.FragmentHomePagerBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.ItemModelHome;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomePagerFragment extends Fragment implements NewsListener {

    private FragmentHomePagerBinding binding;
    private HomePagerAdapter adapter;

    private AppContainer appContainer;

    private boolean isLoading;

    private int[] mNewsIdList;

    private NewsBookmarksDao bookmarksDao;

    private ArrayList<News> bookmarks = new ArrayList<>();


    public HomePagerFragment() {
        // Required empty public constructor
    }

    public static HomePagerFragment newInstance() {
        HomePagerFragment fragment = new HomePagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContainer = ((MyApplication) getActivity().getApplication()).appContainer;

        bookmarksDao = appContainer.room.bookmarksDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomePagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        sendHomePageRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> {
            sendHomePageRequest();
            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> sendHomePageRequest());

    }

    private void sendHomePageRequest() {

        if (isLoading) {
            return;
        }

        isLoading = true;

        binding.imageViewRefresh.setVisibility(View.GONE);

        binding.swipeRefreshLayout.setRefreshing(true);

        appContainer.dataRepository.getHomeNews(new DataRepository.HomeResponseListener() {

            @Override
            public void onResponse(HomePageData data) {

                binding.recyclerView.setVisibility(View.VISIBLE);

                mNewsIdList = MyMethodsClass.initNewsIdListFromHomePage(data);

                //Room
                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                service.execute(() -> {

                    //doInBackgroundThread
                    bookmarks.clear();
                    bookmarks.addAll(bookmarksDao.getBookmarkNews());

                    //onPostExecute
                    handler.post(() -> {

                        adapter.updateList(data, bookmarks);

                        isLoading = false;

                        binding.swipeRefreshLayout.setRefreshing(false);
                    });

                });

                service.shutdown();

            }

            @Override
            public void onFailure(Throwable t) {
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);

                isLoading = false;

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void initRecyclerView() {

        adapter = new HomePagerAdapter(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
         sendHomePageRequest();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        binding = null;
//        appContainer = null;
//
//    }

    @Override
    public void onNewsClicked(int newsId) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", mNewsIdList);
        startActivity(intent);

    }


    @Override
    public void onNewsMenuCommentsClicked(int newsId) {
        Intent intent = new Intent(getActivity(), CommentsActivity.class);
        intent.putExtra("news_id", newsId);
        getContext().startActivity(intent);
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
                handler.post(() -> Toast.makeText(getContext(), "Vest je uspešno uklonjena iz arhive", Toast.LENGTH_SHORT).show());
                news.isInBookmarks = false;
            } else {
                handler.post(() -> Toast.makeText(getContext(), "Vest je uspešno sačuvana u arhivu", Toast.LENGTH_SHORT).show());
                news.isInBookmarks = true;
            }

        });

        service.shutdown();
    }


    @Override
    public void closeOtherMenus() {
        adapter.closeAllMenus();
    }

}