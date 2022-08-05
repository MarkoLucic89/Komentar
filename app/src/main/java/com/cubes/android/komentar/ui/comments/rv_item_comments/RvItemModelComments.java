package com.cubes.android.komentar.ui.comments.rv_item_comments;

import android.content.Context;


import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.data.source.local.database.NewsDatabase;
import com.cubes.android.komentar.databinding.RvItemCommentBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.comments.CommentsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class RvItemModelComments implements ItemModelComments {

    public NewsComment comment;
    public int leftPadding;
    public CommentsListener listener;
    public RvItemCommentBinding binding;

    public RvItemModelComments(NewsComment comment) {
        this.comment = comment;
    }

    public RvItemModelComments(NewsComment comment, CommentsListener listener) {
        this.comment = comment;
        this.listener = listener;
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

        Context context = holder.binding.getRoot().getContext();

        binding.textViewName.setText(comment.name);
        binding.textViewTime.setText(comment.created_at);
        binding.textViewComment.setText(comment.content);
        binding.textViewLike.setText(String.valueOf(comment.positive_votes));
        binding.textViewDislike.setText(String.valueOf(comment.negative_votes));

        if (comment.newsCommentVote == null) {

            setListeners(binding, context);

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


    }

    private void setListeners(RvItemCommentBinding binding, Context context) {

        binding.buttonLike.setOnClickListener(view -> {

            listener.onLikeListener(Integer.parseInt(comment.id), true);

        });

        binding.buttonDislike.setOnClickListener(view -> {

            listener.onDislikeListener(Integer.parseInt(comment.id), false);

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
