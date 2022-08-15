package com.cubes.android.komentar.ui.tag;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsResponseModel;
import com.cubes.android.komentar.data.source.remote.networking.response.TagResponseModel;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemLoadingBinding;
import com.cubes.android.komentar.databinding.RvItemRefreshBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tag.rv_model_tag.ItemModelTag;
import com.cubes.android.komentar.ui.tag.rv_model_tag.RvItemModelTag;
import com.cubes.android.komentar.ui.tag.rv_model_tag.RvItemModelTagLoading;
import com.cubes.android.komentar.ui.tag.rv_model_tag.RvItemModelTagRefresh;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private ArrayList<ItemModelTag> itemModels = new ArrayList<>();
    private NewsListener listener;

    private int[] newsIdList;

    public TagAdapter(NewsListener listener) {
        this.listener = listener;
    }

    public void updateList(NewsResponseModel.NewsDataResponseModel responseModel) {

        itemModels.clear();

        if (responseModel == null) {
            notifyDataSetChanged();
            return;
        }

        newsIdList = MyMethodsClass.initNewsIdList(responseModel.news);

        for (News news : responseModel.news) {
            itemModels.add(new RvItemModelTag(news, listener, newsIdList));
        }

//        if (responseModel.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (responseModel.news.size() == 20) {
            itemModels.add(new RvItemModelTagLoading(listener));
        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public TagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case R.layout.rv_item_category_small:
                binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_loading:
                binding = RvItemLoadingBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_refresh:
                binding = RvItemRefreshBinding.inflate(inflater, parent, false);
                break;
            default:
                binding = null;
        }

        return new TagAdapter.TagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagViewHolder holder, int position) {
        itemModels.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getType();
    }

    public void addNextPage(TagResponseModel.TagDataResponseModel response) {

        int lastIndex;

        if (itemModels.isEmpty()) {
            lastIndex = 0;
        } else {
            lastIndex = itemModels.size() - 1;
            itemModels.remove(lastIndex);
        }

        newsIdList = MyMethodsClass.initNewsIdList(response.news);

        for (News news : response.news) {
            itemModels.add(new RvItemModelTag(news, listener, newsIdList));
        }

//        if (response.data.pagination.has_more_pages) {
//            itemModels.add(new RvItemModelSearchLoading(listener));
//        }

        if (response.news.size() == 20) {
            itemModels.add(new RvItemModelTagLoading(listener));
        }

        notifyItemRangeChanged(lastIndex, itemModels.size());
//        notifyItemRangeInserted((lastIndex + 1), response.news.size());

//        notifyDataSetChanged();
    }

    public void addRefresher() {

        if (itemModels.isEmpty()) {
            return;
        }

        itemModels.remove(itemModels.size() - 1);
        itemModels.add(new RvItemModelTagRefresh(listener));

        notifyItemChanged(itemModels.size() - 1);

    }


    public class TagViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public TagViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}


