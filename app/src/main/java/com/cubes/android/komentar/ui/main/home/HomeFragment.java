package com.cubes.android.komentar.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.data.source.local.SharedPrefs;
import com.cubes.android.komentar.databinding.FragmentHomeBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerAdapter;
import com.cubes.android.komentar.ui.main.home.drawer_menu.OnCategoryClickListener;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerOther;
import com.cubes.android.komentar.ui.subcategories.SubcategoriesActivity;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements
        OnCategoryClickListener,
        RvItemModelDrawerOther.OnPushNotificationListener
{

    private FragmentHomeBinding binding;

    private CategoriesPagerAdapter pagerAdapter;

    private DrawerAdapter adapter;

    private DataRepository dataRepository;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;
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

        initDrawerRecyclerView();

        getAllCategories();

        binding.imageViewDrawerMenu.setOnClickListener(view1 -> binding.getRoot().openDrawer(GravityCompat.END));

    }

    private void initDrawerRecyclerView() {

        boolean isNotificationsOn = SharedPrefs.isNotificationOn(getActivity());

        adapter = new DrawerAdapter(this, this, isNotificationsOn);

        binding.recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewDrawer.setAdapter(adapter);

    }

    private void getAllCategories() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.linearLayoutHome.setVisibility(View.GONE);


        dataRepository.getAllCategories(new DataRepository.CategoriesResponseListener() {
            @Override
            public void onResponse(ArrayList<Category> categories) {

                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.linearLayoutHome.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

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

        pagerAdapter = new CategoriesPagerAdapter(getActivity(), categories);
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
        Intent intent = new Intent(getContext(), SubcategoriesActivity.class);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("subcategory_id", subcategoryId);
        getContext().startActivity(intent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
//        binding = null;
//        dataRepository = null;

    }

    @Override
    public void onPushNotification(boolean isOn) {

        Toast.makeText(getContext(), "NOTIFICATIONS ON: " + isOn, Toast.LENGTH_SHORT).show();

//        SharedPrefs.getInstance(getActivity()).setNotificationStatus(isOn);

        SharedPrefs.setNotificationStatus(getActivity(), isOn);

        if (isOn) {
            FirebaseMessaging.getInstance().subscribeToTopic("main");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("main");
        }

    }

    public boolean onBackPressed() {
        if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END);
            return false;
        } else {
            return true;
        }
    }
}