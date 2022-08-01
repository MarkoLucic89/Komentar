package com.cubes.android.komentar.ui.main.videos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentVideoBinding;
import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class VideoFragment extends Fragment implements LoadNextPageListener {

    private FragmentVideoBinding binding;
    private int page = 1;
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

        initRecyclerView();

        sendVideosRequest();

//        binding.imageViewRefresh.setOnClickListener(view1 -> sendVideosRequest());

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);

            if (page == 1) {
                sendVideosRequest();
            } else {
                loadNextPage();
            }
        });

    }

    private void sendVideosRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        if (binding.recyclerView.getVisibility() == View.GONE) {
            binding.recyclerView.setVisibility(View.VISIBLE);
        }

        DataRepository.getInstance().getVideosFromApi(page, new DataRepository.VideosResponseListener() {


            @Override
            public void onVideosResponse(NewsResponseModel.NewsDataResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                page++;

                adapter.updateList(response);

            }

            @Override
            public void onVideosFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });


    }

    private void initRecyclerView() {
        adapter = new VideosAdapter( this);
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
            public void onVideosResponse(NewsResponseModel.NewsDataResponseModel response) {

                if (binding.recyclerView.getVisibility() == View.GONE) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                binding.progressBar.setVisibility(View.GONE);

                page++;

                adapter.loadNextPage(response);
            }

            @Override
            public void onVideosFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });
    }
}