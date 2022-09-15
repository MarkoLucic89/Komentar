package com.cubes.android.komentar.ui.main.videos;

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

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.local.database.dao.NewsBookmarksDao;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.FragmentVideoBinding;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VideoFragment extends Fragment implements NewsListener {

    private FragmentVideoBinding binding;
    private int nextPage = 1;
    private VideosAdapter adapter;

    private DataRepository dataRepository;

    private NewsBookmarksDao bookmarksDao;

    private ArrayList<News> bookmarks = new ArrayList<>();


    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;

        bookmarksDao = appContainer.room.bookmarksDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVideoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        binding.imageViewRefresh.setVisibility(View.GONE);

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

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);

            initList();

        });

        binding.swipeRefreshLayout.setOnRefreshListener(this::initList);

    }


    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            if (nextPage == 1) {
                initList();
            } else {
                loadNextPage();
            }
        }

    }

    private void initRecyclerView() {
        adapter = new VideosAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    public void initList() {

        nextPage = 1;

        dataRepository.getVideos(nextPage, new DataRepository.VideosResponseListener() {

            @Override
            public void onVideosResponse(ArrayList<News> newsList, boolean hasNextPage) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                MyMethodsClass.checkBookmarks(newsList, bookmarks);

                adapter.initList(newsList, hasNextPage);

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onVideosFailure(Throwable t) {

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
    public void loadNextPage() {

        dataRepository.getVideos(nextPage, new DataRepository.VideosResponseListener() {

            @Override
            public void onVideosResponse(ArrayList<News> newsList, boolean hasNextPage) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                MyMethodsClass.checkBookmarks(newsList, bookmarks);

                adapter.addNextPage(newsList, hasNextPage);

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onVideosFailure(Throwable t) {

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

        dataRepository.getVideos(nextPage, new DataRepository.VideosResponseListener() {

            @Override
            public void onVideosResponse(ArrayList<News> newsList, boolean hasNextPage) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                MyMethodsClass.checkBookmarks(newsList, bookmarks);

                adapter.refreshPage(newsList, hasNextPage);

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onVideosFailure(Throwable t) {

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
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);
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

    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (binding != null) {
//            binding = null;
//        }
//        dataRepository = null;
//
//    }
}