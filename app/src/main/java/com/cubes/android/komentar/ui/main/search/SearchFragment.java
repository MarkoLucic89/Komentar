package com.cubes.android.komentar.ui.main.search;

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
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class SearchFragment extends Fragment implements LoadNextPageListener {

    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private SearchAdapter adapter;
    private String searchTerm;

    private int page = 1;

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

        binding.imageViewSearch.setOnClickListener(view1 -> searchListByTerm());

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
            searchListByTerm();
        });
    }

    private void searchListByTerm() {

        page = 1;

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        searchTerm = binding.editTextSearch.getText().toString().trim();

        if (searchTerm.equals("")) {

            adapter.clearList();

            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        DataRepository.getInstance().searchNews(searchTerm, page, new DataRepository.SearchResponseListener() {
            @Override
            public void onResponse(NewsResponseModel.NewsDataResponseModel response) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

                page++;

                adapter.updateList(response);

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
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            searchListByTerm();
        }

    }

    @Override
    public void loadNextPage() {

        Log.d(TAG, "loadNextPage: " + page);

        DataRepository.getInstance().searchNews(searchTerm, page, new DataRepository.SearchResponseListener() {
            @Override
            public void onResponse(NewsResponseModel.NewsDataResponseModel response) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

                page++;

                adapter.addNextPage(response);

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