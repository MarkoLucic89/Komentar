package com.cubes.android.komentar.ui.main.latest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoryResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.ItemModelCategory;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategoryBig;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategoryLoading;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategoryRefresh;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategorySmall;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<ItemModelCategory> itemModels = new ArrayList<>();
    private boolean isSize5;
    private NewsListener listener;

    public CategoryAdapter(NewsListener listener) {
        isSize5 = false;
        this.listener = listener;
    }

    public CategoryAdapter(ArrayList<News> newsList, boolean isSize5, NewsListener listener) {
        this.isSize5 = isSize5;
        this.listener = listener;
        initList(newsList);
    }

    private void initList(ArrayList<News> newsList) {

        if (newsList.isEmpty()) {
            return;
        }

        //BIG ITEM

        itemModels.add(new RvItemModelCategoryBig(newsList.get(0), isSize5, listener));

        //SMALL ITEMS

        if (newsList.size() <= 1) {
            return;
        }

        for (int i = 1; i < newsList.size(); i++) {
            itemModels.add(new RvItemModelCategorySmall(newsList.get(i), isSize5, listener));
        }

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
        } else if (viewType == 2) {
            binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
        } else {
            binding = RvItemRefreshBinding.inflate(inflater, parent, false);
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


    public void updateList(ArrayList<News> news) {

        for (int i = 0; i < news.size(); i++) {
            if (i == 0) {
                itemModels.add(new RvItemModelCategoryBig(news.get(0), false, listener));
            } else {
                itemModels.add(new RvItemModelCategorySmall(news.get(i), isSize5, listener));
            }
        }

        if (news.size() == 20) {
            itemModels.add(new RvItemModelCategoryLoading(listener));
        }

        notifyDataSetChanged();
    }

    public void addNextPage(NewsResponseModel.NewsDataResponseModel response) {

        int lastIndex = 0;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        for (News news : response.news) {
            itemModels.add(new RvItemModelCategorySmall(news, isSize5, listener));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelCategoryLoading(listener));
//        }

        if (response.news.size() == 20) {
            itemModels.add(new RvItemModelCategoryLoading(listener));
        }

        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted(lastIndex, newsList.size());
//        notifyDataSetChanged();
    }

    public void addNextPage(CategoryResponseModel.CategoryDataResponseModel response) {

        int lastIndex = 0;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        for (News news : response.news) {
            itemModels.add(new RvItemModelCategorySmall(news, isSize5, listener));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelCategoryLoading(listener));
//        }

        if (response.news.size() == 20) {
            itemModels.add(new RvItemModelCategoryLoading(listener));
        }

        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted(lastIndex, newsList.size());
//        notifyDataSetChanged();
    }

    public void addRefresher() {

        if (itemModels.isEmpty()) {
            return;
        }

        itemModels.remove(itemModels.size() - 1);
        itemModels.add(new RvItemModelCategoryRefresh(listener));

        notifyItemChanged(itemModels.size() - 1);


    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public CategoryViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
