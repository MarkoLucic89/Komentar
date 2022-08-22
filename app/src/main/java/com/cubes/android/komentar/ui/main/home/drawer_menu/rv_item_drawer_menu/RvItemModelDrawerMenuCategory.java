package com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer_menu;


import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.CategoryApi;
import com.cubes.android.komentar.databinding.RvItemDrawerMenuCategoryBinding;
import com.cubes.android.komentar.ui.main.home.drawer_menu.OnCategoryClickListener;
import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerMenuAdapter;
import com.cubes.android.komentar.ui.main.home.drawer_menu.SubcategoryAdapter;

import java.util.ArrayList;


public class RvItemModelDrawerMenuCategory implements ItemModelDrawerMenu {

    public CategoryApi category;
    private OnCategoryClickListener listener;

    private ArrayList<CategoryApi> categories;


    public RvItemModelDrawerMenuCategory(CategoryApi category, OnCategoryClickListener listener, ArrayList<CategoryApi> categories) {
        this.category = category;
        this.listener = listener;
        this.categories = categories;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(DrawerMenuAdapter.DrawerMenuViewHolder holder) {

        RvItemDrawerMenuCategoryBinding binding = (RvItemDrawerMenuCategoryBinding) holder.binding;

        //NASLOVNA
        if (category == null) {
            binding.textView.setText("Naslovna");
            binding.viewIndicator.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            binding.imageViewArrow.setVisibility(View.GONE);

            binding.getRoot().setOnClickListener(view -> listener.onCategoryClicked(0));

            return;
        }

        //CATEGORY
        binding.textView.setText(category.name);
        binding.viewIndicator.setBackgroundColor(Color.parseColor(category.color));

        //CATEGORY (nema podkategorija)
        if (category.subcategories.isEmpty()) {

            binding.imageViewArrow.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);

            binding.getRoot().setOnClickListener(view -> {

                int categoryIndex = categories.indexOf(category) + 2;

                //listener
                listener.onCategoryClicked(categoryIndex);
            });
        }
        //CATEGORY (ima podkategorija)
        else {

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
            binding.recyclerView.setAdapter(new SubcategoryAdapter(category.subcategories, category.id));

            if (binding.recyclerView.getVisibility() == View.VISIBLE) {
                binding.imageViewArrow.setImageResource(R.drawable.ic_up);
            } else {
                binding.imageViewArrow.setImageResource(R.drawable.ic_down);
            }

            binding.imageViewArrow.setVisibility(View.VISIBLE);

            binding.getRoot().setOnClickListener(view -> {
                if (binding.recyclerView.getVisibility() == View.VISIBLE) {

                    binding.recyclerView.setVisibility(View.GONE);
                    binding.imageViewArrow.setImageResource(R.drawable.ic_down);

                } else {

                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.imageViewArrow.setImageResource(R.drawable.ic_up);

                }
            });

        }

    }
}
