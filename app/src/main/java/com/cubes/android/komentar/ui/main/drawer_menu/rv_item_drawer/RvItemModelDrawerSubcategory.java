package com.cubes.android.komentar.ui.main.drawer_menu.rv_item_drawer;



import android.content.Intent;
import android.graphics.Color;
import android.view.View;


import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.RvItemDrawerSubcategoryBinding;
import com.cubes.android.komentar.ui.category.CategoryActivity;
import com.cubes.android.komentar.ui.main.drawer_menu.DrawerAdapter;
import com.cubes.android.komentar.ui.main.listeners.OnCategoryClickListener;

public class RvItemModelDrawerSubcategory implements ItemModelDrawer {

    public Category subcategory;
    public Category parentCategory;
    public DrawerAdapter adapter;
    private RvItemDrawerSubcategoryBinding binding;
    private OnCategoryClickListener listener;

    public RvItemModelDrawerSubcategory(Category subcategory, Category parentCategory, OnCategoryClickListener listener) {
        this.subcategory = subcategory;
        this.parentCategory = parentCategory;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void bind(DrawerAdapter.DrawerViewHolder holder) {

        binding = (RvItemDrawerSubcategoryBinding) holder.binding;

        binding.viewIndicator.setBackgroundColor(Color.parseColor(parentCategory.color));
        binding.textView.setText(subcategory.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CategoryActivity.class);
                intent.putExtra("category_id", parentCategory.id);
                intent.putExtra("subcategory_id", subcategory.id);
                view.getContext().startActivity(intent);
            }
        });


    }




}
