package com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer;


import android.graphics.Color;
import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.RvItemDrawerCategoryBinding;
import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerAdapter;
import com.cubes.android.komentar.ui.main.home.drawer_menu.OnCategoryClickListener;

import java.util.ArrayList;

public class RvItemModelDrawerCategory implements ItemModelDrawer {

    public Category category;
    public DrawerAdapter adapter;
    public boolean isOpen = false;
    private OnCategoryClickListener listener;

    private ArrayList<Category> categories;

    public ArrayList<RvItemModelDrawerSubcategory> subcategoryItems;

    public RvItemModelDrawerCategory(Category category, DrawerAdapter adapter, OnCategoryClickListener listener, ArrayList<Category> categories) {
        this.category = category;
        this.adapter = adapter;
        this.listener = listener;
        this.categories = categories;

        subcategoryItems = new ArrayList<>();

        for (Category subcategory : category.subcategories) {
            subcategoryItems.add(new RvItemModelDrawerSubcategory(subcategory, category, listener));
        }
    }

    @Override
    public int getType() {
        return R.layout.rv_item_drawer_category;
    }

    @Override
    public void bind(DrawerAdapter.DrawerViewHolder holder) {

        RvItemDrawerCategoryBinding binding = (RvItemDrawerCategoryBinding) holder.binding;

        binding.textView.setText(category.name);
        binding.viewIndicator.setBackgroundColor(Color.parseColor(category.color));

        if (category.subcategories.isEmpty()) {

            binding.imageViewArrow.setVisibility(View.GONE);

        } else {

            if (isOpen) {
                binding.imageViewArrow.setImageResource(R.drawable.ic_up);
            } else {
                binding.imageViewArrow.setImageResource(R.drawable.ic_down);
            }

            binding.imageViewArrow.setVisibility(View.VISIBLE);
        }

        binding.getRoot().setOnClickListener(view -> {

            if (category.subcategories.isEmpty()) {

                int categoryIndex = categories.indexOf(category) + 2;

                //listener
                listener.onCategoryClicked(categoryIndex);

            } else {

                adapter.updateCategoriesForItem(this);
                isOpen = !isOpen;

            }
        });

    }

}
