package com.cubes.android.komentar.ui.main.videos;

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
import com.cubes.android.komentar.databinding.FragmentVideoBinding;
import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;

import java.util.ArrayList;



public class VideoFragment extends Fragment implements LoadNextPageListener {

    private FragmentVideoBinding binding;
    private int page;
    private VideosAdapter adapter;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
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

        sendVideosRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> sendVideosRequest());

    }

    private void sendVideosRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().getVideosFromApi(page, new DataRepository.VideosResponseListener() {
            @Override
            public void onVideosResponse( NewsResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                ArrayList<News> videos = response.data.news;

                initRecyclerView(videos);
            }

            @Override
            public void onVideosFailure(Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });


    }

    private void initRecyclerView(ArrayList<News> videos) {
        adapter = new VideosAdapter(videos, this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendVideosRequest();
        }

    }

    @Override
    public void loadNextPage() {


        DataRepository.getInstance().getVideosFromApi(page, new DataRepository.VideosResponseListener() {
            @Override
            public void onVideosResponse( NewsResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                page++;

                adapter.loadNextPage(response);
            }

            @Override
            public void onVideosFailure(Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });
    }
}