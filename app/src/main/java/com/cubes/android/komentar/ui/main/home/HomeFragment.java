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
import com.cubes.android.komentar.databinding.FragmentHomeBinding;
import com.cubes.android.komentar.ui.main.listeners.OnCategoryClickListener;
import com.cubes.android.komentar.ui.category.NewsCategoriesViewPagerAdapter;
import com.cubes.android.komentar.ui.main.drawer_menu.DrawerMenuAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment implements OnCategoryClickListener {

    private FragmentHomeBinding binding;

    private NewsCategoriesViewPagerAdapter pagerAdapter;

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

        initViewPager();
        binding.imageViewDrawerMenu.setOnClickListener(view1 -> binding.getRoot().openDrawer(GravityCompat.END));
        initDrawerRecyclerView();

    }


    private void initDrawerRecyclerView() {

//        DrawerAdapter adapter = new DrawerAdapter(this.getActivity(), this);

        DrawerMenuAdapter adapter = new DrawerMenuAdapter(this.getActivity(), this);
        binding.recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewDrawer.setAdapter(adapter);

    }

    private void initViewPager() {

        pagerAdapter = new NewsCategoriesViewPagerAdapter(getActivity());
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
                        tab.setText(DataContainer.categories.get(position - 2).name);
                    }

                }
        ).attach();

    }


    @Override
    public void onCategoryClicked(int categoryIndex) {
        binding.viewPager.setCurrentItem(categoryIndex);
        binding.getRoot().closeDrawer(GravityCompat.END);
    }
}