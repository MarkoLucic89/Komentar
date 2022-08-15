package com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer;


import android.graphics.Color;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.RvItemDrawerSubcategoryBinding;
import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerAdapter;
import com.cubes.android.komentar.ui.main.home.drawer_menu.OnCategoryClickListener;

public class RvItemModelDrawerSubcategory implements ItemModelDrawer {

    public Category subcategory;
    public Category parentCategory;
    public DrawerAdapter adapter;
    private RvItemDrawerSubcategoryBinding binding;
    private OnCategoryClickListener listener;

    private int[] subcategoryIdList;

    public RvItemModelDrawerSubcategory(Category subcategory, Category parentCategory, OnCategoryClickListener listener) {
        this.subcategory = subcategory;
        this.parentCategory = parentCategory;
        this.listener = listener;

        subcategoryIdList = new int[parentCategory.subcategories.size()];

        for (int i = 0; i < parentCategory.subcategories.size(); i++) {
            subcategoryIdList[i] = parentCategory.subcategories.get(i).id;
        }
    }

    @Override
    public int getType() {
        return R.layout.rv_item_drawer_subcategory;
    }

    @Override
    public void bind(DrawerAdapter.DrawerViewHolder holder) {

        binding = (RvItemDrawerSubcategoryBinding) holder.binding;

        binding.viewIndicator.setBackgroundColor(Color.parseColor(parentCategory.color));
        binding.textView.setText(subcategory.name);

        holder.itemView.setOnClickListener(view -> listener.onSubCategoryClicked(parentCategory.id, subcategory.id));


    }




}
