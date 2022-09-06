package com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer;


import android.graphics.Color;
import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.databinding.RvItemDrawerCategoryBinding;
import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerAdapter;
import com.cubes.android.komentar.ui.main.home.drawer_menu.OnCategoryClickListener;

import java.util.ArrayList;

public class RvItemModelDrawerCategory implements ItemModelDrawer {

    public Category mCategory;
    public DrawerAdapter adapter;
    public boolean isOpen = false;
    private OnCategoryClickListener categoryClickListener;
    private OnArrowClickListener arrowClickListener;

    private ArrayList<Category> categories;

    public RvItemModelDrawerCategory(Category category, OnCategoryClickListener categoryClickListener, OnArrowClickListener arrowClickListener, ArrayList<Category> categories) {

        this.mCategory = category;
        this.categoryClickListener = categoryClickListener;
        this.arrowClickListener = arrowClickListener;
        this.categories = categories;

    }

    public interface OnArrowClickListener {
        void onArrowClicked(RvItemModelDrawerCategory item, Category category, boolean isOpen);
    }

    @Override
    public int getType() {
        return R.layout.rv_item_drawer_category;
    }


    @Override
    public void bind(DrawerAdapter.DrawerViewHolder holder) {

        RvItemDrawerCategoryBinding binding = (RvItemDrawerCategoryBinding) holder.binding;

        binding.textView.setText(mCategory.name);
        binding.viewIndicator.setBackgroundColor(Color.parseColor(mCategory.color));

        if (mCategory.subcategories == null || mCategory.subcategories.isEmpty()) {

            binding.imageViewArrow.setVisibility(View.GONE);

        } else {

            if (isOpen) {
                binding.imageViewArrow.setImageResource(R.drawable.ic_up);
            } else {
                binding.imageViewArrow.setImageResource(R.drawable.ic_down);
            }

            binding.imageViewArrow.setVisibility(View.VISIBLE);
        }

//        binding.getRoot().setOnClickListener(view -> {
//
//            if (mCategory.subcategories.isEmpty()) {
//
//                int categoryIndex = categories.indexOf(mCategory) + 2;
//
//                categoryClickListener.onCategoryClicked(categoryIndex);
//
//            } else {
//
//                isOpen = !isOpen;
//
//                arrowClickListener.onArrowClicked(this, mCategory, isOpen);
//
//            }
//        });

        binding.getRoot().setOnClickListener(view -> {

            int categoryIndex = categories.indexOf(mCategory) + 2;

            categoryClickListener.onCategoryClicked(categoryIndex);

        });

        binding.imageViewArrow.setOnClickListener(view -> {

            isOpen = !isOpen;

            arrowClickListener.onArrowClicked(this, mCategory, isOpen);

        });

    }

    @Override
    public int getId() {
        return mCategory.id;
    }

}
