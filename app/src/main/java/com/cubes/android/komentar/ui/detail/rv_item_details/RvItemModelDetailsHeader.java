package com.cubes.android.komentar.ui.detail.rv_item_details;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.NewsDetails;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.RvItemDetailsHeaderBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

public class RvItemModelDetailsHeader implements ItemModelDetails {

    private NewsDetails newsDetails;

    public RvItemModelDetailsHeader(NewsDetails newsDetails) {
        this.newsDetails = newsDetails;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_details_header;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsHeaderBinding binding = (RvItemDetailsHeaderBinding) holder.binding;

//        binding.textViewTitle.setText(newsDetails.title);
//        binding.textViewDateTime.setText(MyMethodsClass.convertDateTime(newsDetails.created_at));
//        binding.textViewCommentsCount.setText(String.valueOf(newsDetails.comments_count));
//        binding.textViewAuthor.setText(newsDetails.author_name);
//        binding.textViewSource.setText(newsDetails.source);
//        binding.textViewDescription.setText(newsDetails.description);

    }
}
