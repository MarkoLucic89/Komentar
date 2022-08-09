package com.cubes.android.komentar.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.FragmentHomeBinding;
import com.cubes.android.komentar.ui.category.CategoryActivity;
import com.cubes.android.komentar.ui.category.NewsCategoriesViewPagerAdapter;
import com.cubes.android.komentar.ui.main.drawer_menu.DrawerAdapter;
import com.cubes.android.komentar.ui.main.drawer_menu.DrawerMenuAdapter;
import com.cubes.android.komentar.ui.main.listeners.OnCategoryClickListener;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnCategoryClickListener {

    private FragmentHomeBinding binding;

    private NewsCategoriesViewPagerAdapter pagerAdapter;

    //Rezervni adapter (svaki RvCategoryItem ima recuclerView sa podkategorijama)(ne koristim ga trenutno)
    private DrawerMenuAdapter drawerMenuAdapter;

    private DrawerAdapter adapter;

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

        binding.tabLayout.setVisibility(View.INVISIBLE);
        binding.viewPager.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        initDrawerRecyclerView();

        getAllCategories();

//        initViewPager();
        binding.imageViewDrawerMenu.setOnClickListener(view1 -> binding.getRoot().openDrawer(GravityCompat.END));

    }

    private void initDrawerRecyclerView() {

        adapter = new DrawerAdapter(this.getActivity(), this);

//        drawerMenuAdapter = new DrawerMenuAdapter(this.getActivity(), this);

        binding.recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewDrawer.setAdapter(adapter);

    }

    private void getAllCategories() {

        DataRepository.getInstance().getAllCategories(new DataRepository.CategoriesResponseListener() {
            @Override
            public void onResponse(ArrayList<Category> categories) {

                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.linearLayoutHome.setVisibility(View.VISIBLE);
                binding.tabLayout.setVisibility(View.VISIBLE);
                binding.viewPager.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

//                drawerMenuAdapter.updateList(getActivity(), categories);
                adapter.updateList(categories);
                initViewPager(categories);
            }

            @Override
            public void onFailure(Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.linearLayoutHome.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });
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
    public void onSubCategoryClicked(int categoryId, int subcategoryId) {
        Intent intent = new Intent(getContext(), CategoryActivity.class);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("subcategory_id", subcategoryId);
        getContext().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}