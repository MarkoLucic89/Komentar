package com.cubes.android.komentar.ui.main.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.category_response.CategoryResponseModel;
import com.cubes.android.komentar.databinding.FragmentCategoryBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;

import java.util.ArrayList;



public class NewsFragment extends Fragment implements LoadNextPageListener {

    private final String TAG = "CategoryFragment";

    private int mCategoryId;

    private FragmentCategoryBinding binding;

    private CategoryAdapter categoryAdapter;

    private int page = 0;


    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(int categoryId) {
        NewsFragment fragment = new NewsFragment();
        fragment.mCategoryId = categoryId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> categoryRequest());

    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            categoryRequest();
        }

    }


    private void categoryRequest() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.imageViewRefresh.setVisibility(View.GONE);

        DataRepository.getInstance().getNewsForCategory(mCategoryId, page, new DataRepository.CategoryResponseListener() {
            @Override
            public void onResponse(CategoryResponseModel response) {
                binding.progressBar.setVisibility(View.GONE);

                ArrayList<News> news = response.data.news;
                initRecyclerView(news);
            }

            @Override
            public void onFailure(Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initRecyclerView(ArrayList<News> videos) {

        categoryAdapter = new CategoryAdapter(videos, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void loadNextPage() {


        DataRepository.getInstance().getNewsForCategory(mCategoryId, (page + 1), new DataRepository.CategoryResponseListener() {
            @Override
            public void onResponse(CategoryResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                page++;

                categoryAdapter.loadNextPage(response);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}