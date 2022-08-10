package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.RvItemDetailsCommentsBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;


public class RvItemModelDetailsComments implements ItemModelDetails {

    private NewsDetailsResponseModel.NewsDetailsDataResponseModel data;
    private CommentsAdapter.CommentsListener listener;

    public RvItemModelDetailsComments(CommentsAdapter.CommentsListener listener, NewsDetailsResponseModel.NewsDetailsDataResponseModel data) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_details_comments;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsCommentsBinding binding = (RvItemDetailsCommentsBinding) holder.binding;

        binding.textViewTitle.setText("Komentari (" + data.comments_count + ")");
        binding.textViewCommentsCount.setText(String.valueOf(data.comments_count));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(new CommentsAdapter(listener, data.comments_top_n, true));

        if (data.comments_count > 0) {
            binding.buttonAllComments.setVisibility(View.VISIBLE);
            binding.buttonAllComments.setOnClickListener(view -> listener.goToCommentsActivity(data.id));
        } else {
            binding.buttonAllComments.setVisibility(View.GONE);
        }

        binding.buttonPostComment.setOnClickListener(view -> listener.goOnPostCommentActivity(data.id, 0));

    }


}
