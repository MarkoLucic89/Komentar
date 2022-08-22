package com.cubes.android.komentar.ui.main.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cubes.android.komentar.data.model.CategoryApi;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerFragment;
import com.cubes.android.komentar.ui.main.latest.LatestNewsFragment;

import java.util.ArrayList;

public class CategoriesPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Category> categories;

    public CategoriesPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Category> categories) {
        super(fragmentActivity);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {
            return HomePagerFragment.newInstance();
        }

        if (position == 1) {
            return LatestNewsFragment.newInstance();
        }

        return CategoryNewsFragment.newInstance(categories.get(position - 2).id);
    }

    @Override
    public int getItemCount() {
        return categories.size() + 2;
    }

}
