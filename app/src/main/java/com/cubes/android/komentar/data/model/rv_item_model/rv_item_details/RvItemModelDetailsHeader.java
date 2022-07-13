package com.cubes.android.komentar.data.model.rv_item_model.rv_item_details;

import com.cubes.android.komentar.data.model.response.news_details_response.NewsDetailsDataResponseModel;
import com.cubes.android.komentar.databinding.RvItemDetailsHeaderBinding;
import com.cubes.android.komentar.ui.adapter.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

public class RvItemModelDetailsHeader implements ItemModelDetails {

    private NewsDetailsDataResponseModel newsDetails;

    public RvItemModelDetailsHeader(NewsDetailsDataResponseModel newsDetails) {
        this.newsDetails = newsDetails;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsHeaderBinding binding = (RvItemDetailsHeaderBinding) holder.binding;

        binding.textViewTitle.setText(newsDetails.title);
        binding.textViewDateTime.setText(MyMethodsClass.convertDateTime(newsDetails.created_at));
        binding.textViewCommentsCount.setText(String.valueOf(newsDetails.comments_count));
        binding.textViewAuthor.setText(newsDetails.author_name);
        binding.textViewSource.setText(newsDetails.source);
        binding.textViewDescription.setText(newsDetails.description);

    }
}
