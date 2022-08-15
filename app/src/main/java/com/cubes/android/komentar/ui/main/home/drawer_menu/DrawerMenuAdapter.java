package com.cubes.android.komentar.ui.main.home.drawer_menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer_menu.ItemModelDrawerMenu;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer_menu.RvItemModelDrawerMenuCategory;
import com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer_menu.RvItemModelDrawerMenuOther;
import com.cubes.android.komentar.databinding.RvItemDrawerMenuCategoryBinding;
import com.cubes.android.komentar.databinding.RvItemDrawerOtherBinding;

import java.util.ArrayList;

public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.DrawerMenuViewHolder> {

/*
    OVO JE ALTERNATIVNI ADAPTER KOJI NE KORISTIM, RADJEN JE NA MALO DRUGACIJI NACIN PA SAM GA OSTAVIO
 */

    private ArrayList<ItemModelDrawerMenu> list = new ArrayList<>();
    private OnCategoryClickListener listener;
    private Activity activity;

    public DrawerMenuAdapter(Activity activity, OnCategoryClickListener listener) {
        this.listener = listener;
        this.activity = activity;
    }

    public void updateList(Activity activity, ArrayList<Category> categories) {

        //HOME
        list.add(new RvItemModelDrawerMenuCategory(null, listener, categories));

        //CATEGORIES
        for (Category category : categories) {
            list.add(new RvItemModelDrawerMenuCategory(category, listener, categories));
        }

        //WEATHER
        list.add(new RvItemModelDrawerMenuOther(0));

        //CURRENCY LIST
        list.add(new RvItemModelDrawerMenuOther(1));

        //HOROSCOPE
        list.add(new RvItemModelDrawerMenuOther(2));

        //PUSH NOTIFICATIONS
        list.add(new RvItemModelDrawerMenuOther(3, activity));

        //MARKETING
        list.add(new RvItemModelDrawerMenuOther(4));

        //TERMS AND CONDITIONS
        list.add(new RvItemModelDrawerMenuOther(5));

        //CONTACT
        list.add(new RvItemModelDrawerMenuOther(6));

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public DrawerMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewBinding binding = null;

        switch (viewType) {
            case 0:
                binding = RvItemDrawerMenuCategoryBinding.inflate(inflater, parent, false);
                break;
            default:
                binding = RvItemDrawerOtherBinding.inflate(inflater, parent, false);

        }

        return new DrawerMenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerMenuViewHolder holder, int position) {
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

    public class DrawerMenuViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public DrawerMenuViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}