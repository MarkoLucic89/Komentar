package com.cubes.android.komentar.ui.category;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.ui.main.home.CategoryNewsFragment;
import com.cubes.android.komentar.ui.main.home.HomePagerFragment;
import com.cubes.android.komentar.ui.main.latest.LatestNewsFragment;

import java.util.ArrayList;

public class NewsCategoriesViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Category> categories;
    private ArrayList<Category> subcategories;

    public NewsCategoriesViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Category> categories) {
        super(fragmentActivity);
        this.categories = categories;
        this.subcategories = null;
    }

    public NewsCategoriesViewPagerAdapter(FragmentActivity fragmentActivity, ArrayList<Category> categories, ArrayList<Category> subcategories) {
        super(fragmentActivity);
        this.categories = categories;
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

            categoryId = categories.get(position - 2).id;

        }

        return CategoryNewsFragment.newInstance(categoryId);
    }

    @Override
    public int getItemCount() {
        if (subcategories != null) {
            return subcategories.size();
        }
        return categories.size() + 2;
    }


}
