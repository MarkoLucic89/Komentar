package com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.data.model.News;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<News> newsList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<News> newsList) {
        super(fragmentActivity);
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DetailsFragment.newInstance(newsList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
