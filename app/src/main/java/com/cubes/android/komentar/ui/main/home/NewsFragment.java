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
import com.cubes.android.komentar.data.source.remote.networking.response.CategoryResponseModel;
import com.cubes.android.komentar.databinding.FragmentCategoryBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class NewsFragment extends Fragment implements LoadNextPageListener {

    private static final String ARG_CATEGORY_ID = "category_id";

    private int mCategoryId;

    private FragmentCategoryBinding binding;

    private CategoryAdapter categoryAdapter;

    private int page = 1;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(int categoryId) {
        NewsFragment fragment = new NewsFragment();
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
        categoryRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);

            if (page == 1) {
                categoryRequest();
            } else {
                loadNextPage();
            }

        });

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
            public void onResponse(CategoryResponseModel.CategoryDataResponseModel response) {
                    binding.progressBar.setVisibility(View.GONE);

                    page++;

                    if (binding.recyclerView.getVisibility() == View.GONE) {
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }

                    if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
                        binding.imageViewRefresh.setVisibility(View.GONE);
                    }

                    categoryAdapter.updateList(response);

                }


            @Override
            public void onFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

            }
        });

    }

    private void initRecyclerView() {

        categoryAdapter = new CategoryAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void loadNextPage() {


        DataRepository.getInstance().getNewsForCategory(mCategoryId, page, new DataRepository.CategoryResponseListener() {


            @Override
            public void onResponse(CategoryResponseModel.CategoryDataResponseModel response) {


                if (binding.recyclerView.getVisibility() == View.GONE) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
                    binding.imageViewRefresh.setVisibility(View.GONE);
                }

                binding.progressBar.setVisibility(View.GONE);

                page++;

                categoryAdapter.loadNextPage(response);
            }

            @Override
            public void onFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}