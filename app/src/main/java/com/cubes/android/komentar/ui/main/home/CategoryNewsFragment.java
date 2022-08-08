package com.cubes.android.komentar.ui.main.home;

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
import com.cubes.android.komentar.data.source.remote.networking.response.CategoryResponseModel;
import com.cubes.android.komentar.databinding.FragmentCategoryBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsActivity;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class CategoryNewsFragment extends Fragment implements NewsListener {

    private static final String ARG_CATEGORY_ID = "category_id";

    private int mCategoryId;

    private FragmentCategoryBinding binding;

    private CategoryAdapter categoryAdapter;

    private int nextPage = 1;

    public CategoryNewsFragment() {
        // Required empty public constructor
    }

    public static CategoryNewsFragment newInstance(int categoryId) {
        CategoryNewsFragment fragment = new CategoryNewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        fragment.mCategoryId = categoryId;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(ARG_CATEGORY_ID);
        }
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

        initRecyclerView();

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.imageViewRefresh.setVisibility(View.GONE);
        loadNextPage();

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);

            loadNextPage();

        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.imageViewRefresh.setVisibility(View.GONE);
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

        DataRepository.getInstance().getNewsForCategory(mCategoryId, nextPage, new DataRepository.CategoryNewsResponseListener() {

            @Override
            public void onResponse(CategoryResponseModel.CategoryDataResponseModel response) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);

                if (nextPage == 1) {
                    categoryAdapter.updateList(response.news);
                } else {
                    categoryAdapter.addNextPage(response);
                }

                nextPage++;

            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                } else {
                    categoryAdapter.addRefresher();
                }

            }
        });
    }

    @Override
    public void onNewsClicked(int newsId) {
        Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
        intent.putExtra("news_id", newsId);
        getContext().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}