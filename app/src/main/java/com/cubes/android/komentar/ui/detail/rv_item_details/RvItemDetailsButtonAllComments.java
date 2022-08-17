package com.cubes.android.komentar.ui.detail.rv_item_details;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemDetailsAllCommentsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsCommentsBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;


public class RvItemDetailsButtonAllComments implements ItemModelDetails {

    private int newsId;
    private int commentCount;
    private CommentsAdapter.CommentsListener listener;

    public RvItemDetailsButtonAllComments(int newsId, int commentCount, CommentsAdapter.CommentsListener listener) {
        this.newsId = newsId;
        this.commentCount = commentCount;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_details_all_comments;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsAllCommentsBinding binding = (RvItemDetailsAllCommentsBinding) holder.binding;

        binding.textViewCommentsCount.setText(String.valueOf(commentCount));

        binding.buttonAllComments.setOnClickListener(view -> listener.goToCommentsActivity(newsId));

    }


}
