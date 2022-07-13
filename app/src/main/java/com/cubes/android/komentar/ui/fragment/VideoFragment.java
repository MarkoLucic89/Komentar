package com.cubes.android.komentar.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.response.news_response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentVideoBinding;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.VideosAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFragment extends Fragment {

    private FragmentVideoBinding binding;

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

        NewsApi.getInstance().getNewsService().getVideos().enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                ArrayList<News> videos = response.body().data.news;

                initRecyclerView(videos);
            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);            }
        });
    }

    private void initRecyclerView(ArrayList<News> videos) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new VideosAdapter(videos));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendVideosRequest();
        }

    }
}