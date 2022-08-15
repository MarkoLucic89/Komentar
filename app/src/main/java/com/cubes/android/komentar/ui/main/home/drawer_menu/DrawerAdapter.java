package com.cubes.android.komentar.ui.main.home.drawer_menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.ItemModelDrawer;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerCategory;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerHome;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer.RvItemModelDrawerOther;
import com.cubes.android.komentar.databinding.RvItemDrawerCategoryBinding;
import com.cubes.android.komentar.databinding.RvItemDrawerOtherBinding;
import com.cubes.android.komentar.databinding.RvItemDrawerSubcategoryBinding;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {


    /*
    (NEMA RECYCLERVIEW ZA PODKATEGORIJE VEC SE DODAJU NOVI ITEMI U ADAPTERU)
     */

    private ArrayList<ItemModelDrawer> list;
    private OnCategoryClickListener listener;
    private Activity activity;

    public DrawerAdapter(Activity activity, OnCategoryClickListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
        this.activity = activity;


    }

    public void updateList(ArrayList<Category> categories) {
        list.add(new RvItemModelDrawerHome(listener));

        for (Category category : categories) {
            list.add(new RvItemModelDrawerCategory(category, this, listener, categories));
        }

        list.add(new RvItemModelDrawerOther("Vremenska prognoza", true));
        list.add(new RvItemModelDrawerOther("Kursna lista", false));
        list.add(new RvItemModelDrawerOther("Horoskop", false));

        list.add(new RvItemModelDrawerOther(activity, true, "Push notifikacije", true));
        list.add(new RvItemModelDrawerOther("Marketing", false));
        list.add(new RvItemModelDrawerOther("Uslovi korišćenja", false));
        list.add(new RvItemModelDrawerOther("Kontakt", false, true));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrawerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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

        return new DrawerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerViewHolder holder, int position) {
        list.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    public void setListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void updateCategoriesForItem(RvItemModelDrawerCategory item) {

        if (item.isOpen) {
            list.removeAll(item.subcategoryItems);
        } else {
            this.list.addAll(list.indexOf(item) + 1, item.subcategoryItems);
        }

        notifyDataSetChanged();
    }

    public class DrawerViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public DrawerViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
