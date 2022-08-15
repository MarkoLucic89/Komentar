package com.cubes.android.komentar.ui.main.home.drawer_menu;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.RvItemDrawerMenuSubcategoryBinding;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewModel> {

    private ArrayList<Category> subcategories;
    private int categoryId;

    public SubcategoryAdapter(ArrayList<Category> subcategories, int categoryId) {
        this.subcategories = subcategories;
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public SubcategoryViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubcategoryViewModel(
                RvItemDrawerMenuSubcategoryBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryViewModel holder, int position) {
        Category subcategory = subcategories.get(position);
        holder.binding.textView.setText(subcategory.name);

        holder.binding.getRoot().setOnClickListener(view -> {
            MyMethodsClass.goToSubcategoriesActivity(view, categoryId, subcategory.id);
        });
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public class SubcategoryViewModel extends RecyclerView.ViewHolder {

        public RvItemDrawerMenuSubcategoryBinding binding;

        public SubcategoryViewModel(@NonNull RvItemDrawerMenuSubcategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
