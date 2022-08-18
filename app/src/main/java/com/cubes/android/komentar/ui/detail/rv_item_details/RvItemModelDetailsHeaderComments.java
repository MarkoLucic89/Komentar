package com.cubes.android.komentar.ui.detail.rv_item_details;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemDetailsTitleBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;

public class RvItemModelDetailsHeaderComments implements ItemModelDetails {

    private final int commentCount;
    private final int newsId;
    private CommentsAdapter.CommentsListener listener;

    public RvItemModelDetailsHeaderComments(int commentCount, int newsId, CommentsAdapter.CommentsListener listener) {
        this.commentCount = commentCount;
        this.newsId = newsId;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_details_title;
    }


    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsTitleBinding binding = (RvItemDetailsTitleBinding) holder.binding;

        binding.textViewTitle.setText("KOMENTARI (" + commentCount + ")");
        binding.viewIndicator.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.blue_dark));

        binding.buttonPostComment.setOnClickListener(view -> listener.goOnPostCommentActivity(newsId, 0));

    }
}
