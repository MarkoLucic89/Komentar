package com.cubes.android.komentar.ui.main.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.news_response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentSearchBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment{

    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private SearchAdapter adapter;
    private ArrayList<News> filteredList;

    private int page;

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

        filteredList = new ArrayList<>();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SearchAdapter();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.imageViewSearch.setOnClickListener(view1 -> searchListByTerm());
        binding.imageViewRefresh.setOnClickListener(view1 -> searchListByTerm());
    }

    private void searchListByTerm() {

        filteredList.clear();

        String searchTerm = binding.editTextSearch.getText().toString().trim();

        if (searchTerm.equals("")) {
            adapter.updateList(filteredList);
            return;
        }

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().searchNews(searchTerm, page, new DataRepository.SearchResponseListener() {
            @Override
            public void onResponse(NewsResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                filteredList = response.data.news;

                updateAdapterList(searchTerm, page, response);


            }

            @Override
            public void onFailure(Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

            }
        });

    }

    private void updateAdapterList(String searchTerm, int page, NewsResponseModel response) {

        adapter.updateList(response, new LoadNextPageListener() {
            @Override
            public void loadNextPage() {

                DataRepository.getInstance().searchNews(searchTerm, SearchFragment.this.page, new DataRepository.SearchResponseListener() {
                    @Override
                    public void onResponse(NewsResponseModel response) {

                        binding.progressBar.setVisibility(View.GONE);

                        filteredList = response.data.news;

                        updateAdapterList(searchTerm, page, response);


                    }

                    @Override
                    public void onFailure(Throwable t) {

                        binding.progressBar.setVisibility(View.GONE);
                        binding.imageViewRefresh.setVisibility(View.VISIBLE);

                    }
                });
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

}