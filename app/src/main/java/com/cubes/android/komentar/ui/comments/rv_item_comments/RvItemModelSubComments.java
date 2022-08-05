package com.cubes.android.komentar.ui.comments.rv_item_comments;

import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.databinding.RvItemCommentBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.comments.CommentsListener;

public class RvItemModelSubComments implements ItemModelComments {

    public NewsComment comment;
    public RvItemCommentBinding binding;
    public CommentsListener listener;

    public RvItemModelSubComments(NewsComment comment, CommentsListener listener) {
        this.comment = comment;
        this.listener = listener;
    }

    @Override
    public int getCommentsId() {
        return Integer.parseInt(comment.id);
    }

    @Override
    public int getType() {
        return 1;
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

            setListeners(binding);

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
            listener.goOnPostCommentActivity(view.getContext(), Integer.parseInt(comment.news), Integer.parseInt(comment.id));
        });

        setMargins(binding.layoutRoot, 80, 0, 0, 0);

    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void setListeners(RvItemCommentBinding binding) {

        binding.buttonLike.setOnClickListener(view -> listener.onLikeListener(Integer.parseInt(comment.id), true));

        binding.buttonDislike.setOnClickListener(view -> listener.onDislikeListener(Integer.parseInt(comment.id), false));

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
