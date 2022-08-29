package com.cubes.android.komentar.ui.main.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.FragmentSearchBinding;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements NewsListener {

    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private SearchAdapter adapter;
    private String searchTerm;

    private int nextPage = 1;

    private AppContainer appContainer;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
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
            binding.progressBar.setVisibility(View.VISIBLE);
            searchListByTerm();
            hideKeyboard(getActivity());
        });

        binding.editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    adapter.clearList();
                    searchListByTerm();
                    hideKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            binding.imageViewRefresh.setVisibility(View.GONE);

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
            searchListByTerm();
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> searchListByTerm());
    }

    private void searchListByTerm() {

        nextPage = 1;

        searchTerm = binding.editTextSearch.getText().toString().trim();

        if (searchTerm.equals("")) {

            binding.swipeRefreshLayout.setRefreshing(false);

            adapter.clearList();

            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("search_term", searchTerm);
        FirebaseAnalytics.getInstance(getContext()).logEvent("search_news", bundle);

        loadNextPage();

    }


    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {

            binding.imageViewRefresh.setVisibility(View.GONE);

            searchListByTerm();
        }

    }

    @Override
    public void loadNextPage() {

        Log.d(TAG, "loadNextPage: " + nextPage);

        appContainer.dataRepository.searchNews(searchTerm, nextPage, new DataRepository.SearchResponseListener() {


            @Override
            public void onResponse(ArrayList<News> newsList, boolean hasMorePages) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

                if (nextPage == 1) {
                    adapter.updateList(newsList, hasMorePages);
                } else {
                    adapter.addNextPage(newsList, hasMorePages);
                }

                nextPage++;

                binding.swipeRefreshLayout.setRefreshing(false);

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

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onNewsClicked(int newsId, int[] newsIdList) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        appContainer = null;

    }
}