package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.databinding.RvItemCommentBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.comments.rv_item_comments.ItemModelComments;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;


public class RvItemModelDetailComment implements ItemModelDetails {

    public NewsComment comment;
    public CommentsAdapter.CommentsListener listener;
    public RvItemCommentBinding binding;
    private boolean isSubComment;

    public RvItemModelDetailComment(NewsComment comment, CommentsAdapter.CommentsListener listener, boolean isSubComment) {
        this.comment = comment;
        this.listener = listener;
        this.isSubComment = isSubComment;
    }

    @Override
    public int getCommentsId() {
        return Integer.parseInt(comment.id);
    }

    @Override
    public int getType() {
        return R.layout.rv_item_comment;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        binding = (RvItemCommentBinding) holder.binding;

        binding.textViewName.setText(comment.name);
        binding.textViewTime.setText(comment.created_at);
        binding.textViewComment.setText(comment.content);
        binding.textViewLike.setText(String.valueOf(comment.positive_votes));
        binding.textViewDislike.setText(String.valueOf(comment.negative_votes));

        if (comment.newsCommentVote == null) {

            binding.buttonLike.setOnClickListener(view -> {
                listener.onLikeListener(Integer.parseInt(comment.id), true);
                binding.buttonLike.setEnabled(false);
                binding.buttonDislike.setEnabled(false);
            });

            binding.buttonDislike.setOnClickListener(view -> {
                listener.onDislikeListener(Integer.parseInt(comment.id), false);
                binding.buttonLike.setEnabled(false);
                binding.buttonDislike.setEnabled(false);
            });

        } else {

            binding.buttonLike.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Već ste glasali za ovaj komentar.", Toast.LENGTH_SHORT).show();
                binding.buttonLike.setEnabled(false);
                binding.buttonDislike.setEnabled(false);
            });

            binding.buttonDislike.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Već ste glasali za ovaj komentar.", Toast.LENGTH_SHORT).show();
                binding.buttonLike.setEnabled(false);
                binding.buttonDislike.setEnabled(false);
            });

            if (comment.newsCommentVote.vote) {
                binding.buttonLike.setBackgroundResource(R.drawable.background_button_like);
            } else {
                binding.buttonDislike.setBackgroundResource(R.drawable.background_button_dislike);
            }
        }

        binding.buttonReply.setOnClickListener(view -> {
            listener.goOnPostCommentActivity(Integer.parseInt(comment.news), Integer.parseInt(comment.id));
        });

        if (isSubComment) {
            setMargins(binding.layoutRoot, 80, 0, 0, 0);
        }

    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
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
