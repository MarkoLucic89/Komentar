package com.cubes.android.komentar.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.ui.activity.CategoryActivity;
import com.cubes.android.komentar.ui.fragment.CategoryFragment;
import com.cubes.android.komentar.ui.fragment.HomePagerFragment;
import com.cubes.android.komentar.ui.fragment.LatestNewsFragment;

import java.util.ArrayList;

public class NewsCategoriesViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Category> subcategories;

    public NewsCategoriesViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.subcategories = null;
    }

    public NewsCategoriesViewPagerAdapter(CategoryActivity fragmentActivity, ArrayList<Category> subcategories) {
        super(fragmentActivity);
        this.subcategories = subcategories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int categoryId;

        if (subcategories != null) {

            categoryId = subcategories.get(position).id;

        } else {

            if (position == 0) {
                return HomePagerFragment.newInstance();
            }

            if (position == 1) {
                return LatestNewsFragment.newInstance();
            }

            categoryId = DataContainer.categories.get(position - 2).id;

        }

        return CategoryFragment.newInstance(categoryId);
    }

    @Override
    public int getItemCount() {
        if (subcategories != null) {
            return subcategories.size();
        }
        return DataContainer.categories.size() + 2;
    }


}
