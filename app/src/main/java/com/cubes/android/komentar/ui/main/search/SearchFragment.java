package com.cubes.android.komentar.ui.main.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentSearchBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class SearchFragment extends Fragment implements NewsListener {

    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private SearchAdapter adapter;
    private String searchTerm;

    private int nextPage = 1;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SearchAdapter(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.imageViewSearch.setOnClickListener(view1 -> {
            adapter.clearList();
            searchListByTerm();
        });

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
            searchListByTerm();
        });
    }

    private void searchListByTerm() {

        nextPage = 1;

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        searchTerm = binding.editTextSearch.getText().toString().trim();

        if (searchTerm.equals("")) {

            adapter.clearList();

            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        loadNextPage();

    }


    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            searchListByTerm();
        }

    }

    @Override
    public void loadNextPage() {

        Log.d(TAG, "loadNextPage: " + nextPage);

        DataRepository.getInstance().searchNews(searchTerm, nextPage, new DataRepository.SearchResponseListener() {
            @Override
            public void onResponse(NewsResponseModel.NewsDataResponseModel response) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

                nextPage++;

                adapter.addNextPage(response);

            }

            @Override
            public void onFailure(Throwable t) {

                if (nextPage == 1) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewRefresh.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    adapter.addRefresher();
                }

            }
        });

    }

    @Override
    public void onNewsClicked(int newsId) {
        Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
        intent.putExtra("news_id", newsId);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}