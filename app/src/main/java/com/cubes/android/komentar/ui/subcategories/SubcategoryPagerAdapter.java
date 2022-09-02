package com.cubes.android.komentar.ui.subcategories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cubes.android.komentar.ui.main.home.CategoryNewsFragment;

public class SubcategoryPagerAdapter extends FragmentStateAdapter {

    private final int[] idList;

    private static final String TAG = "SubcategoryPagerAdapter";

    public SubcategoryPagerAdapter(@NonNull FragmentActivity fragmentActivity, int[] idList) {
        super(fragmentActivity);
        this.idList = idList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(TAG, "createFragment: POSITION " + position);
        return CategoryNewsFragment.newInstance(idList[position], true);
    }

    @Override
    public int getItemCount() {
        return idList.length;
    }
}
