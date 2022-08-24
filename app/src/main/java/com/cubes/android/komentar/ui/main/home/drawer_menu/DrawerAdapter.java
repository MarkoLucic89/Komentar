package com.cubes.android.komentar.ui.main.home.drawer_menu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> implements RvItemModelDrawerCategory.OnArrowClickListener {

    /*
    (NEMA RECYCLERVIEW ZA PODKATEGORIJE VEC SE DODAJU NOVI ITEMI U ADAPTERU)
     */

    private ArrayList<ItemModelDrawer> list;
    private OnCategoryClickListener categoryClickListener;
    private RvItemModelDrawerOther.OnPushNotificationListener notificationListener;
    private boolean isNotificationsOn;

    private static final String TAG = "DrawerAdapter";

    public DrawerAdapter(OnCategoryClickListener categoryClickListener, RvItemModelDrawerOther.OnPushNotificationListener notificationListener, boolean isNotificationsOn) {
        this.list = new ArrayList<>();
        this.categoryClickListener = categoryClickListener;
        this.notificationListener = notificationListener;
        this.isNotificationsOn = isNotificationsOn;
    }

    public void updateList(ArrayList<Category> categories) {

        list.clear();

        list.add(new RvItemModelDrawerHome(categoryClickListener));

        for (Category category : categories) {
            list.add(new RvItemModelDrawerCategory(category, categoryClickListener, this, categories));
        }

        list.add(new RvItemModelDrawerOther("Vremenska prognoza"));
        list.add(new RvItemModelDrawerOther("Kursna lista"));
        list.add(new RvItemModelDrawerOther("Horoskop"));

        list.add(new RvItemModelDrawerOther(notificationListener, "Push notifikacije", isNotificationsOn));
        list.add(new RvItemModelDrawerOther("Marketing"));
        list.add(new RvItemModelDrawerOther("Uslovi korišćenja"));
        list.add(new RvItemModelDrawerOther("Kontakt"));

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
        this.categoryClickListener = listener;
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


        notifyDataSetChanged();
    }

    public static class DrawerViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public DrawerViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
