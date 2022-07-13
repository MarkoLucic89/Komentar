package com.cubes.android.komentar.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_category.ItemModelCategory;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_category.RvItemModelCategoryBig;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_category.RvItemModelCategoryLoading;
import com.cubes.android.komentar.data.model.rv_item_model.rv_model_category.RvItemModelCategorySmall;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;
import com.cubes.android.komentar.listeners.UpdateCategoryListListener;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<ItemModelCategory> itemModels;
    private boolean isSize5;
    private UpdateCategoryListListener listener;

    public CategoryAdapter(ArrayList<News> newsList, UpdateCategoryListListener listener) {
        isSize5 = false;
        this.listener = listener;
        initList(newsList);
    }

    public CategoryAdapter(ArrayList<News> newsList, boolean isSize5) {
        this.isSize5 = isSize5;
        initList(newsList);
    }

    private void initList(ArrayList<News> newsList) {

        itemModels = new ArrayList<>();

        if (newsList.isEmpty()) {
            return;
        }

        //BIG ITEM

        itemModels.add(new RvItemModelCategoryBig(newsList.get(0), isSize5));

        //SMALL ITEMS

        if (newsList.size() <= 1) {
            return;
        }

        for (int i = 1; i < newsList.size(); i++) {
            itemModels.add(new RvItemModelCategorySmall(newsList.get(i), isSize5));
        }

        itemModels.add(new RvItemModelCategoryLoading(this, listener));

    }


    @NonNull
    @Override

    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            binding = RvItemCategoryBigBinding.inflate(inflater, parent, false);
        } else if (viewType == 1) {
            binding = RvItemLoadingBinding.inflate(inflater, parent, false);
        } else {
            binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
        }
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        itemModels.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        if (isSize5 && itemModels.size() > 5) {
            return 5;
        }
        return itemModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getType();
    }

    public void loadNextPage(ArrayList<News> newsList) {

        int lastIndex = itemModels.size() - 1;

        itemModels.remove(lastIndex);

        for (News news: newsList) {
            itemModels.add(new RvItemModelCategorySmall(news, isSize5));
        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelCategoryLoading(this, listener));
        }

        notifyItemRangeChanged(lastIndex, itemModels.size());

//        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public CategoryViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
