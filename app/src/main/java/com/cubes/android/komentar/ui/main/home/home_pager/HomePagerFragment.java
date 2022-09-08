package com.cubes.android.komentar.ui.main.home.home_pager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.data.model.domain.HomePageData;
import com.cubes.android.komentar.databinding.FragmentHomePagerBinding;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.ItemModelHome;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

public class HomePagerFragment extends Fragment implements NewsListener {

    private FragmentHomePagerBinding binding;
    private HomePagerAdapter adapter;

    private AppContainer appContainer;

    private boolean isLoading;

    private int[] mNewsIdList;

    public HomePagerFragment() {
        // Required empty public constructor
    }

    public static HomePagerFragment newInstance() {
        HomePagerFragment fragment = new HomePagerFragment();
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

        binding = FragmentHomePagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        sendHomePageRequest();

        binding.imageViewRefresh.setOnClickListener(view1 -> {
            sendHomePageRequest();
            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> sendHomePageRequest());

    }

    private void refreshListOnSwipe() {

        appContainer.dataRepository.getHomeNews(new DataRepository.HomeResponseListener() {

            @Override
            public void onResponse(HomePageData data) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                adapter.updateList(data);

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void sendHomePageRequest() {

        if (isLoading) {
            return;
        }

        isLoading = true;

        binding.imageViewRefresh.setVisibility(View.GONE);

        binding.swipeRefreshLayout.setRefreshing(true);

        appContainer.dataRepository.getHomeNews(new DataRepository.HomeResponseListener() {

            @Override
            public void onResponse(HomePageData data) {

                binding.recyclerView.setVisibility(View.VISIBLE);

                mNewsIdList = MyMethodsClass.initNewsIdListFromHomePage(data);

                adapter.updateList(data);

                isLoading = false;

                binding.swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Throwable t) {
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);

                isLoading = false;

                binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void initRecyclerView() {

        adapter = new HomePagerAdapter(this);

        binding.recyclerView.setItemViewCacheSize(50);

//        binding.recyclerView.setViewCacheExtension(new RecyclerView.ViewCacheExtension() {
//            @Nullable
//            @Override
//            public View getViewForPositionAndType(@NonNull RecyclerView.Recycler recycler, int position, int type) {
//                return null;
//            }
//        });

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

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        binding = null;
//        appContainer = null;
//
//    }

    @Override
    public void onNewsClicked(int newsId) {

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", mNewsIdList);
        startActivity(intent);

    }

}