package com.cubes.android.komentar.ui.detail;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int[] newsIdList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int[] newsIdList) {
        super(fragmentActivity);
        this.newsIdList = newsIdList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return NewsDetailsFragment.newInstance(newsIdList[position]);
    }

    @Override
    public int getItemCount() {
        return newsIdList.length;
    }
}
