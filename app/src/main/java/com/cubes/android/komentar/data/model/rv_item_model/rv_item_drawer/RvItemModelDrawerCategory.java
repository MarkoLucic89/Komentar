package com.cubes.android.komentar.data.model.rv_item_model.rv_item_drawer;


import android.graphics.Color;
import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.RvItemDrawerCategoryBinding;
import com.cubes.android.komentar.listeners.OnCategoryClickListener;
import com.cubes.android.komentar.ui.adapter.DrawerAdapter;

import java.util.ArrayList;

public class RvItemModelDrawerCategory implements ItemModelDrawer {

    public Category category;
    public DrawerAdapter adapter;
    public boolean isOpen = false;
    private OnCategoryClickListener listener;

    public ArrayList<RvItemModelDrawerSubcategory> subcategoryItems;

    public RvItemModelDrawerCategory(Category category, DrawerAdapter adapter, OnCategoryClickListener listener) {
        this.category = category;
        this.adapter = adapter;
        this.listener = listener;

        subcategoryItems = new ArrayList<>();

        for (Category subcategory : category.subcategories) {
            subcategoryItems.add(new RvItemModelDrawerSubcategory(subcategory, category));
        }
    }

    @Override
    public int getType() {
        return 0;
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

                int categoryIndex = DataContainer.categories.indexOf(category) + 2;

                //listener
                listener.onCategoryClicked(categoryIndex);

            } else {

                adapter.updateCategoriesForItem(this);
                isOpen = !isOpen;

            }
        });

    }

}
