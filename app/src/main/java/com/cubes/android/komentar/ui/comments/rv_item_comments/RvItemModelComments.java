package com.cubes.android.komentar.ui.comments.rv_item_comments;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.databinding.RvItemCommentBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;


public class RvItemModelComments implements ItemModelComments {

    public NewsComment comment;
    public CommentsAdapter adapter;
    public CommentsAdapter.CommentsListener listener;
    public RvItemCommentBinding binding;

    public RvItemModelComments(NewsComment comment, CommentsAdapter.CommentsListener listener, CommentsAdapter adapter) {
        this.comment = comment;
        this.listener = listener;
        this.adapter = adapter;
    }

    @Override
    public int getCommentsId() {
        return Integer.parseInt(comment.id);
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(CommentsAdapter.CommentsViewHolder holder) {

        binding = (RvItemCommentBinding) holder.binding;

        binding.textViewName.setText(comment.name);
        binding.textViewTime.setText(comment.created_at);
        binding.textViewComment.setText(comment.content);
        binding.textViewLike.setText(String.valueOf(comment.positive_votes));
        binding.textViewDislike.setText(String.valueOf(comment.negative_votes));

        if (comment.newsCommentVote == null) {

            binding.buttonLike.setOnClickListener(view ->
                    listener.onLikeListener(adapter, Integer.parseInt(comment.id), true));

            binding.buttonDislike.setOnClickListener(view ->
                    listener.onDislikeListener(adapter, Integer.parseInt(comment.id), false));

        } else {

            binding.buttonLike.setEnabled(false);
            binding.buttonDislike.setEnabled(false);

            if (comment.newsCommentVote.vote) {
                binding.buttonLike.setBackgroundResource(R.drawable.background_button_like);
            } else {
                binding.buttonDislike.setBackgroundResource(R.drawable.background_button_dislike);
            }
        }

        binding.buttonReply.setOnClickListener(view -> {
            listener.goOnPostCommentActivity(Integer.parseInt(comment.news), Integer.parseInt(comment.id));
        });

    }

    @Override
    public void updateLikeUi() {

        int likes = Integer.parseInt(binding.textViewLike.getText().toString());
        likes++;
        binding.textViewLike.setText(String.valueOf(likes));

        binding.buttonLike.setEnabled(false);
        binding.buttonDislike.setEnabled(false);

        binding.buttonLike.setBackgroundResource(R.drawable.background_button_like);
    }

    @Override
    public void updateDislikeUi() {

        int dislikes = Integer.parseInt(binding.textViewDislike.getText().toString());
        dislikes++;
        binding.textViewDislike.setText(String.valueOf(dislikes));

        binding.buttonLike.setEnabled(false);
        binding.buttonDislike.setEnabled(false);

        binding.buttonDislike.setBackgroundResource(R.drawable.background_button_dislike);

    }

}
