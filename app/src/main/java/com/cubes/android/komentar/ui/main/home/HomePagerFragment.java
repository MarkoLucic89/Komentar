package com.cubes.android.komentar.ui.main.home;

import android.content.Intent;
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
import com.cubes.android.komentar.data.source.remote.networking.response.HomePageResponseModel;
import com.cubes.android.komentar.databinding.FragmentHomePagerBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsActivity;
import com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;

import java.util.ArrayList;

public class HomePagerFragment extends Fragment implements NewsListener {

    private FragmentHomePagerBinding binding;
    private HomeAdapter adapter;
    private int[] newsIDs;

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

        initRecyclerView();

        sendHomePageRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> sendHomePageRequest());

    }

    private void sendHomePageRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().getHomeNews(new DataRepository.HomeResponseListener() {

            @Override
            public void onResponse(HomePageResponseModel.HomePageDataResponseModel response) {
                binding.progressBar.setVisibility(View.GONE);
                adapter.updateList(response);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void initRecyclerView() {
        adapter = new HomeAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendHomePageRequest();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onNewsClicked(int newsId) {
        Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
        intent.putExtra("news_id", newsId);
        getActivity().startActivity(intent);
    }

    @Override
    public void onNewsClicked(int newsId, String newsUrl, ArrayList<News> newsList) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);


        int[] newsIdList = new int[newsList.size()];

        for (int i = 0; i < newsList.size(); i++) {
            newsIdList[i] = newsList.get(i).id;
        }

        intent.putExtra("news_id", newsId);

        intent.putExtra("news_url", newsUrl);

        intent.putExtra("news_id_list", newsIdList);

        startActivity(intent);
    }
}