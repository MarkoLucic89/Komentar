package com.cubes.android.komentar.ui.main.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.data.source.remote.networking.NewsApi;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoriesResponseModel;
import com.cubes.android.komentar.databinding.FragmentHomeBinding;
import com.cubes.android.komentar.ui.main.listeners.OnCategoryClickListener;
import com.cubes.android.komentar.ui.category.NewsCategoriesViewPagerAdapter;
import com.cubes.android.komentar.ui.main.drawer_menu.DrawerMenuAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnCategoryClickListener {

    private FragmentHomeBinding binding;

    private NewsCategoriesViewPagerAdapter pagerAdapter;

    private ArrayList<Category> categories;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsApi.getInstance().getNewsService().getCategories().enqueue(new Callback<CategoriesResponseModel>() {
            @Override
            public void onResponse(Call<CategoriesResponseModel> call, Response<CategoriesResponseModel> response) {

                categories = response.body().data;

                initDrawerRecyclerView(categories);
                initViewPager(categories);
            }

            @Override
            public void onFailure(Call<CategoriesResponseModel> call, Throwable t) {

            }
        });

//        initViewPager();
        binding.imageViewDrawerMenu.setOnClickListener(view1 -> binding.getRoot().openDrawer(GravityCompat.END));

    }


    private void initDrawerRecyclerView(ArrayList<Category> categories) {

//        DrawerAdapter adapter = new DrawerAdapter(this.getActivity(), this, categories);

        DrawerMenuAdapter adapter = new DrawerMenuAdapter(this.getActivity(), this, categories);
        binding.recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewDrawer.setAdapter(adapter);

    }

    private void initViewPager(ArrayList<Category> categories) {

        pagerAdapter = new NewsCategoriesViewPagerAdapter(getActivity(), categories);
        binding.viewPager.setAdapter(pagerAdapter);



        new TabLayoutMediator(
                binding.tabLayout,
                binding.viewPager,
                (tab, position) -> {

                    if (position == 0) {
                        tab.setText("Naslovna");
                    } else if (position == 1) {
                        tab.setText("Najnovije");
                    } else {
                        tab.setText(categories.get(position - 2).name);
                    }

                }
        ).attach();

    }


    @Override
    public void onCategoryClicked(int categoryIndex) {
        binding.viewPager.setCurrentItem(categoryIndex);
        binding.getRoot().closeDrawer(GravityCompat.END);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}