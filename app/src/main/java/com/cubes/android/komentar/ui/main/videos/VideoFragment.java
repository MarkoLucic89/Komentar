package com.cubes.android.komentar.ui.main.videos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.databinding.FragmentVideoBinding;
import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.ui.detail.NewsDetailsActivity;
import com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;


public class VideoFragment extends Fragment implements NewsListener {

    private FragmentVideoBinding binding;
    private int nextPage = 1;
    private VideosAdapter adapter;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVideoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

//        sendVideosRequest();
        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        loadNextPage();

        binding.imageViewRefresh.setOnClickListener(view1 -> {

            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);

            loadNextPage();
        });

    }

    private void initRecyclerView() {
        adapter = new VideosAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            loadNextPage();
        }

    }

    @Override
    public void loadNextPage() {

        DataRepository.getInstance().getVideosFromApi(nextPage, new DataRepository.VideosResponseListener() {

            @Override
            public void onVideosResponse(NewsResponseModel.NewsDataResponseModel response) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                nextPage++;

                adapter.addNextPage(response);
            }

            @Override
            public void onVideosFailure(Throwable t) {

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
        getActivity().startActivity(intent);
    }

    @Override
    public void onNewsClicked(int newsId, String newsUrl, ArrayList<News> newsList) {

        int[] newsIdList = new int[newsList.size()];

        for (int i = 0; i < newsList.size(); i++) {
            newsIdList[i] = newsList.get(i).id;
        }

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_url", newsUrl);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}