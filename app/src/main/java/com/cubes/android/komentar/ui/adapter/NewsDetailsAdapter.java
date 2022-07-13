package com.cubes.android.komentar.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.response.news_details_response.NewsDetailsDataResponseModel;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_details.ItemModelDetails;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_details.RvItemModelDetailsComments;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_details.RvItemModelDetailsHeader;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_details.RvItemModelDetailsRelatedNews;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_details.RvItemModelDetailsTags;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_details.RvItemModelDetailsWebView;
import com.cubes.android.komentar.databinding.RvItemDetailsCommentsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsHeaderBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsRelatedNewsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsTagsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsWebViewBinding;

import java.util.ArrayList;

public class NewsDetailsAdapter extends RecyclerView.Adapter<NewsDetailsAdapter.NewsDetailsViewHolder> {

    private ArrayList<ItemModelDetails> list;

    public NewsDetailsAdapter(NewsDetailsDataResponseModel newsDetails) {
        list = new ArrayList<>();

        //HEADER
        list.add(new RvItemModelDetailsHeader(newsDetails));

        //WEB VIEW
        list.add(new RvItemModelDetailsWebView(newsDetails.url));

        //TAGS
        list.add(new RvItemModelDetailsTags(newsDetails.tags));

        //COMMENTS
        list.add(new RvItemModelDetailsComments(newsDetails));

        //RELATED NEWS
        list.add(new RvItemModelDetailsRelatedNews(newsDetails.related_news));
    }

    @NonNull
    @Override
    public NewsDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewBinding binding;

        switch (viewType) {
            case 0:
                binding = RvItemDetailsHeaderBinding.inflate(inflater, parent, false);
                break;
            case 1:
                binding = RvItemDetailsWebViewBinding.inflate(inflater, parent, false);
                break;
            case 2:
                binding = RvItemDetailsTagsBinding.inflate(inflater, parent, false);
                break;
            case 3:
                binding = RvItemDetailsCommentsBinding.inflate(inflater, parent, false);
                break;
            case 4:
                binding = RvItemDetailsRelatedNewsBinding.inflate(inflater, parent, false);
                break;
            default:
                binding = null;

        }
        return new NewsDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDetailsViewHolder holder, int position) {
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

    public class NewsDetailsViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public NewsDetailsViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
