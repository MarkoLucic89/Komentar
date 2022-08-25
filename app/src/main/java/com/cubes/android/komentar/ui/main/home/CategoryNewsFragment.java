package com.cubes.android.komentar.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.FragmentCategoryBinding;
import com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;


public class CategoryNewsFragment extends Fragment implements NewsListener {

    private static final String ARG_CATEGORY_ID = "category_id";

    private int mCategoryId;

    private FragmentCategoryBinding binding;

    private CategoryAdapter categoryAdapter;

    private int nextPage = 1;

    private FirebaseAnalytics mFirebaseAnalytics;

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

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

        binding.swipeRefreshLayout.setOnRefreshListener(() -> refreshAdapter());

    }

    private void refreshAdapter() {
        nextPage = 1;
        loadNextPage();
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
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);

                if (nextPage == 1) {

                    categoryAdapter.updateList(newsList, hasMorePages);

                    Category category = newsList.get(0).category;

                    if (category.type.equalsIgnoreCase("category")) {

                        Bundle bundle = new Bundle();
                        bundle.putString("category_name", newsList.get(0).category.name);
                        mFirebaseAnalytics.logEvent("categories", bundle);

                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putString("subcategory_name", newsList.get(0).category.name);
                        mFirebaseAnalytics.logEvent("subcategories", bundle);

                    }


                } else {
                    categoryAdapter.addNextPage(newsList, hasMorePages);
                }

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);

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

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onNewsClicked(int newsId, String newsUrl, int[] newsIdList) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_url", newsUrl);        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);

    }

}