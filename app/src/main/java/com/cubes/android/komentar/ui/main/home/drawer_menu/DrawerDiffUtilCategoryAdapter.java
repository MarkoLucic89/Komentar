package com.cubes.android.komentar.ui.main.home.drawer_menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.CategoryApi;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.databinding.RvItemDrawerCategoryBinding;
import com.cubes.android.komentar.databinding.RvItemDrawerOtherBinding;
import com.cubes.android.komentar.databinding.RvItemDrawerSubcategoryBinding;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.ItemModelDrawer;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerCategory;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerHome;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerOther;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerSubcategory;

import java.util.ArrayList;

public class DrawerDiffUtilCategoryAdapter extends ListAdapter<ItemModelDrawer, DrawerDiffUtilCategoryAdapter.DrawerViewHolder> implements RvItemModelDrawerCategory.OnArrowClickListener {

    /*
    NE KORISTI SE TRENUTNO
     */

    private ArrayList<ItemModelDrawer> list = new ArrayList<>();

    private Activity activity;
    private OnCategoryClickListener categoryClickListener;

    public DrawerDiffUtilCategoryAdapter(
            @NonNull DiffUtil.ItemCallback<ItemModelDrawer> diffCallback,
            Activity activity,
            OnCategoryClickListener categoryClickListener
    ) {
        super(diffCallback);

        this.activity = activity;
        this.categoryClickListener = categoryClickListener;

    }

    public void setData(ArrayList<Category> categories) {

        list.clear();

        list.add(new RvItemModelDrawerHome(categoryClickListener));

        for (Category category : categories) {
            list.add(new RvItemModelDrawerCategory(category, categoryClickListener, this, categories));
        }

        list.add(new RvItemModelDrawerOther("Vremenska prognoza", true));
        list.add(new RvItemModelDrawerOther("Kursna lista", false));
        list.add(new RvItemModelDrawerOther("Horoskop", false));

        list.add(new RvItemModelDrawerOther(activity, true, "Push notifikacije", true));
        list.add(new RvItemModelDrawerOther("Marketing", false));
        list.add(new RvItemModelDrawerOther("Uslovi korišćenja", false));
        list.add(new RvItemModelDrawerOther("Kontakt", false, true));

        submitList(list);

    }

    @NonNull
    @Override
    public DrawerDiffUtilCategoryAdapter.DrawerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewBinding binding;

        switch (viewType) {
            case R.layout.rv_item_drawer_category:
                binding = RvItemDrawerCategoryBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_drawer_subcategory:
                binding = RvItemDrawerSubcategoryBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_drawer_other:
                binding = RvItemDrawerOtherBinding.inflate(inflater, parent, false);
                break;
            default:
                binding = null;
        }

        return new DrawerDiffUtilCategoryAdapter.DrawerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerDiffUtilCategoryAdapter.DrawerViewHolder holder, int position) {
//        list.get(position).bind(holder);
    }

    @Override
    public void onArrowClicked(RvItemModelDrawerCategory item, Category category, boolean isOpen) {

        if (isOpen) {

            ArrayList<RvItemModelDrawerSubcategory> subcategoryItems = new ArrayList<>();

            for (Category subcategory : category.subcategories) {
                subcategoryItems.add(new RvItemModelDrawerSubcategory(subcategory, category, categoryClickListener));
            }

            list.addAll(list.indexOf(item) + 1, subcategoryItems);

        } else {

            int startPosition = list.indexOf(item) + 1;
            int endPosition = startPosition + category.subcategories.size();

            ArrayList<RvItemModelDrawerSubcategory> subcategoryItems = new ArrayList<>();


            for (int i = startPosition; i < endPosition; i++) {
                subcategoryItems.add((RvItemModelDrawerSubcategory) list.get(i));
            }

            list.removeAll(subcategoryItems);

        }

        submitList(list);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    public class DrawerViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public DrawerViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
