package com.cubes.android.komentar.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.response.category_response.CategoryResponseModel;
import com.cubes.android.komentar.data.model.response.news_response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentLatestNewsBinding;
import com.cubes.android.komentar.listeners.UpdateCategoryListListener;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.CategoryAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestNewsFragment extends Fragment implements UpdateCategoryListListener {

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

        NewsApi.getInstance().getNewsService().getLatest(page, 50).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                ArrayList<News> latest = response.body().data.news;

                initRecyclerView(latest);
            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

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

        page++;

        NewsApi.getInstance().getNewsService().getLatest(page, 50).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {

                ArrayList<News> news = response.body().data.news;
                categoryAdapter.loadNextPage(news);

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {

            }
        });

//        Toast.makeText(this.getContext(), "LOAD PAGE " + page  , Toast.LENGTH_SHORT).show();


    }
}