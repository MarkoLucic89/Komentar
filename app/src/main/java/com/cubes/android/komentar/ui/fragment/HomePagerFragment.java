package com.cubes.android.komentar.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.model.response.home_response.HomePageResponseModel;
import com.cubes.android.komentar.databinding.FragmentHomePagerBinding;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.HomeAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePagerFragment extends Fragment {

    private FragmentHomePagerBinding binding;

    public HomePagerFragment() {
        // Required empty public constructor
    }

    public static HomePagerFragment newInstance() {
        HomePagerFragment fragment = new HomePagerFragment();
        return fragment;
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

        sendHomePageRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> sendHomePageRequest());

    }

    private void sendHomePageRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        NewsApi.getInstance().getNewsService().getHomePageData().enqueue(new Callback<HomePageResponseModel>() {
            @Override
            public void onResponse(Call<HomePageResponseModel> call, Response<HomePageResponseModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                initRecyclerView(response);

            }

            @Override
            public void onFailure(Call<HomePageResponseModel> call, Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

            }
        });

    }

    private void initRecyclerView(Response<HomePageResponseModel> response) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new HomeAdapter(response));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendHomePageRequest();
        }

    }
}