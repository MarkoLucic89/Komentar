package com.cubes.android.komentar.ui.main.latest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentLatestNewsBinding;
import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

public class LatestNewsFragment extends Fragment implements LoadNextPageListener {

    private FragmentLatestNewsBinding binding;

    private CategoryAdapter categoryAdapter;

    private int nextPage = 1;


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

        initRecyclerView();

//        sendLatestRequest();

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        loadNextPage();

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);

//            if (page == 1) {
//                sendLatestRequest();
//            } else {
//                loadNextPage();
//            }

            loadNextPage();

        });

    }

//    private void sendLatestRequest() {
//
//        binding.imageViewRefresh.setVisibility(View.GONE);
//        binding.progressBar.setVisibility(View.VISIBLE);
//
//        DataRepository.getInstance().getLatest(page, new DataRepository.LatestResponseListener() {
//
//            @Override
//            public void onResponse(NewsResponseModel.NewsDataResponseModel response) {
//
//                binding.recyclerView.setVisibility(View.VISIBLE);
//                binding.imageViewRefresh.setVisibility(View.GONE);
//
//                page++;
//
//                binding.progressBar.setVisibility(View.GONE);
//
//                categoryAdapter.addNextPage(response);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//                binding.recyclerView.setVisibility(View.GONE);
//                binding.imageViewRefresh.setVisibility(View.VISIBLE);
//                binding.progressBar.setVisibility(View.GONE);
//            }
//        });
//
//    }

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

        DataRepository.getInstance().getLatest(nextPage, new DataRepository.LatestResponseListener() {

            @Override
            public void onResponse(NewsResponseModel.NewsDataResponseModel response) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);

                nextPage++;

                categoryAdapter.addNextPage(response);
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

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}