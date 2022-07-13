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
import com.cubes.android.komentar.databinding.FragmentCategoryBinding;
import com.cubes.android.komentar.listeners.UpdateCategoryListListener;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.CategoryAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment implements UpdateCategoryListListener {

    private final String TAG = "CategoryFragment";

    private int mCategoryId;

    private FragmentCategoryBinding binding;

    private CategoryAdapter categoryAdapter;

    private int page = 0;


    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(int categoryId) {
        CategoryFragment fragment = new CategoryFragment();
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

        NewsApi.getInstance().getNewsService().getCategoryNews(mCategoryId, page, 5).enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                ArrayList<News> news = response.body().data.news;
                initRecyclerView(news);

            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {
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

        page++;

        NewsApi.getInstance().getNewsService().getCategoryNews(mCategoryId, page, 50).enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {

                ArrayList<News> news = response.body().data.news;
                categoryAdapter.loadNextPage(news);

            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {

            }
        });



//        Toast.makeText(this.getContext(), "LOAD PAGE " + page  , Toast.LENGTH_SHORT).show();
    }
}