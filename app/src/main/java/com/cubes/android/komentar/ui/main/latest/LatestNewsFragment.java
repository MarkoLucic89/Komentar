package com.cubes.android.komentar.ui.main.latest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.di.AppContainer;
import com.cubes.android.komentar.data.di.MyApplication;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.FragmentLatestNewsBinding;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class LatestNewsFragment extends Fragment implements NewsListener {

    private FragmentLatestNewsBinding binding;

    private CategoryAdapter categoryAdapter;

    private int nextPage = 1;

    private AppContainer appContainer;


    public LatestNewsFragment() {
        // Required empty public constructor
    }

    public static LatestNewsFragment newInstance() {
        LatestNewsFragment fragment = new LatestNewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLatestNewsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        loadNextPage();

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);

            loadNextPage();

        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> refreshAdapter());

    }

    private void refreshAdapter() {

        nextPage = 1;

        loadNextPage();
//
//        DataRepository.getInstance().getLatest(nextPage, new DataRepository.LatestResponseListener() {
//
//            @Override
//            public void onResponse(NewsResponseModel.NewsDataResponseModel response) {
//
//                binding.recyclerView.setVisibility(View.VISIBLE);
//                binding.imageViewRefresh.setVisibility(View.GONE);
//                binding.progressBar.setVisibility(View.GONE);
//
//                if (nextPage == 1) {
//                    categoryAdapter.updateList(response.news);
//                } else {
//                    categoryAdapter.addNextPage(response);
//                }
//
//                nextPage++;
//
//                binding.swipeRefreshLayout.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//                if (nextPage == 1) {
//                    binding.recyclerView.setVisibility(View.GONE);
//                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
//                    binding.progressBar.setVisibility(View.GONE);
//                } else {
//                    categoryAdapter.addRefresher();
//                }
//
//                binding.swipeRefreshLayout.setRefreshing(false);
//
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            binding.imageViewRefresh.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            loadNextPage();
        }

    }

    private void initRecyclerView() {

        categoryAdapter = new CategoryAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void loadNextPage() {

        appContainer.dataRepository.getLatestNews(nextPage, new DataRepository.LatestResponseListener() {

            @Override
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);

                if (nextPage == 1) {
                    categoryAdapter.updateList(newsList, hasMorePages);
                } else {
                    categoryAdapter.addNextPage(newsList, hasMorePages);
                }

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    categoryAdapter.addRefresher();
                }

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void onNewsClicked(int newsId, String newsUrl, int[] newsIdList) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_url", newsUrl);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}