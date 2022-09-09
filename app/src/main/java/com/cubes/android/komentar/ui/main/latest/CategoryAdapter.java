package com.cubes.android.komentar.ui.main.latest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.ItemModelCategory;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategoryBig;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategoryLoading;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategoryRefresh;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelCategorySmall;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.RvItemModelHomeAd;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<ItemModelCategory> itemModels = new ArrayList<>();
    private boolean isOnHomePage;
    private NewsListener listener;

    private int[] newsIdList;

    private int adCounter = 0;

    public CategoryAdapter(NewsListener listener) {
        isOnHomePage = false;
        this.listener = listener;
    }

    @NonNull
    @Override

    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == R.layout.rv_item_category_big) {
            binding = RvItemCategoryBigBinding.inflate(inflater, parent, false);
        } else if (viewType == R.layout.rv_item_category_small) {
            binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
        } else if (viewType == R.layout.rv_item_loading) {
            binding = RvItemLoadingBinding.inflate(inflater, parent, false);
        } else if (viewType == R.layout.rv_item_refresh) {
            binding = RvItemRefreshBinding.inflate(inflater, parent, false);
        } else if (viewType == R.layout.rv_item_ad) {
            binding = RvItemAdBinding.inflate(inflater, parent, false);
        } else {
            binding = null;
        }
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        itemModels.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        if (isOnHomePage && itemModels.size() > 5) {
            return 5;
        }
        return itemModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getType();
    }


    public void initList(ArrayList<News> newsList, boolean hasMorePages) {

        adCounter = 0;

        itemModels.clear();

        newsIdList = MyMethodsClass.initNewsIdList(newsList);

        for (int i = 0; i < newsList.size(); i++) {

            if (i == 0) {

                itemModels.add(new RvItemModelCategoryBig(newsList.get(0), false, listener, newsIdList));

            } else {

                if ((i - 1) % 5 == 0) {

                    itemModels.add(new RvItemModelHomeAd());

                    adCounter++;

                }

                itemModels.add(new RvItemModelCategorySmall(newsList.get(i), false, listener, newsIdList));

            }
        }

        if (newsList.size() == 20) {

            itemModels.add(new RvItemModelCategoryLoading(listener));

        }

        notifyDataSetChanged();
    }

    public void addNextPage(ArrayList<News> newsList, boolean hasMorePages) {

        if (newsList.isEmpty()) {

            itemModels.remove(itemModels.size() - 1);
            notifyItemRemoved(itemModels.size());
            return;

        }

        int lastIndex = 0;

        if (!itemModels.isEmpty()) {

            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);

        }

        newsIdList = MyMethodsClass.initNewsIdList(newsList);

        for (int i = 0; i < newsList.size(); i++) {

            itemModels.add(new RvItemModelCategorySmall(newsList.get(i), isOnHomePage, listener, newsIdList));

            if (i == 0 && adCounter < 5) {

                itemModels.add(new RvItemModelHomeAd());

                adCounter++;

            }
        }

//        if (hasMorePages) {
//            itemModels.add(new RvItemModelCategoryLoading(listener));
//        }

        if (newsList.size() == 20) {
            itemModels.add(new RvItemModelCategoryLoading(listener));
        }

//        notifyItemRangeChanged(lastIndex, itemModels.size());
        notifyItemRangeInserted(lastIndex + 1, newsList.size());
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
