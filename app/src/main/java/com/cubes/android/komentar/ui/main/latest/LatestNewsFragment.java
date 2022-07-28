package com.cubes.android.komentar.ui.main.latest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.news_response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentLatestNewsBinding;
import com.cubes.android.komentar.data.DataRepository;

import java.util.ArrayList;

public class LatestNewsFragment extends Fragment implements LoadNextPageListener {

    private FragmentLatestNewsBinding binding;

    private CategoryAdapter categoryAdapter;

    private int page = 0;


    public LatestNewsFragment() {
        // Required empty public constructor
    }

    public static LatestNewsFragment newInstance() {
        LatestNewsFragment fragment = new LatestNewsFragment();
        return fragment;
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

        sendLatestRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> sendLatestRequest());

    }

    private void sendLatestRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().getLatest(page, new DataRepository.LatestResponseListener() {
            @Override
            public void onResponse(NewsResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                ArrayList<News> latest = response.data.news;

                initRecyclerView(latest);
            }

            @Override
            public void onFailure(Throwable t) {
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendLatestRequest();
        }

    }


    private void initRecyclerView(ArrayList<News> latest) {

        categoryAdapter = new CategoryAdapter(latest, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void loadNextPage() {


        DataRepository.getInstance().getLatest((page + 1), new DataRepository.LatestResponseListener() {
            @Override
            public void onResponse(NewsResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                page++;

                categoryAdapter.loadNextPage(response);
            }

            @Override
            public void onFailure(Throwable t) {
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }
}