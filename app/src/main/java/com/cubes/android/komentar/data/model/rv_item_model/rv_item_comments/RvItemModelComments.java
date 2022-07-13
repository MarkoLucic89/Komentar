package com.cubes.android.komentar.data.model.rv_item_model.rv_item_comments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import androidx.room.RoomDatabase;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.cubes.android.komentar.database.NewsDatabase;
import com.cubes.android.komentar.databinding.RvItemCommentBinding;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.CommentsAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RvItemModelComments implements ItemModelComments {

    public NewsComment comment;
    public int leftPadding;

    public RvItemModelComments(NewsComment comment) {
        this.comment = comment;
    }

    public RvItemModelComments(NewsComment comment, int leftPadding) {
        this.comment = comment;
        this.leftPadding = leftPadding;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(CommentsAdapter.CommentsViewHolder holder) {

        RvItemCommentBinding binding = (RvItemCommentBinding) holder.binding;

        Context context = holder.binding.getRoot().getContext();

        binding.textViewName.setText(comment.name);
        binding.textViewTime.setText(comment.created_at);
        binding.textViewComment.setText(comment.content);
        binding.textViewLike.setText(String.valueOf(comment.positive_votes));
        binding.textViewDislike.setText(String.valueOf(comment.negative_votes));


        NewsCommentVote commentVote = NewsDatabase.getInstance(context).voteDao().getNewsCommentVote(comment.id);

        if (commentVote == null) {

            setListeners(binding, context);

        } else {

            binding.buttonLike.setEnabled(false);
            binding.buttonDislike.setEnabled(false);

            if (commentVote.vote) {
                binding.buttonLike.setBackgroundResource(R.drawable.background_button_like);
            } else {
                binding.buttonDislike.setBackgroundResource(R.drawable.background_button_dislike);
            }
        }

        binding.buttonReply.setOnClickListener(view -> {
            MyMethodsClass.goToPostCommentsActivity(view, Integer.parseInt(comment.news), Integer.parseInt(comment.id));
        });



        setMargins(binding.layoutRoot, leftPadding, 0, 0, 0);

    }

    private void setListeners(RvItemCommentBinding binding, Context context) {

        binding.buttonLike.setOnClickListener(view -> {

            NewsApi.getInstance().getNewsService().postLike(Integer.parseInt(comment.id), true)
                    .enqueue(new Callback<NewsCommentVote>() {
                        @Override
                        public void onResponse(Call<NewsCommentVote> call, Response<NewsCommentVote> response) {
                            if (response.isSuccessful()) {
//                                Toast.makeText(context, "USPESNO: " + response.body().vote, Toast.LENGTH_SHORT).show();

                                int likes = Integer.parseInt(binding.textViewLike.getText().toString());
                                likes++;
                                binding.textViewLike.setText(String.valueOf(likes));

                                NewsCommentVote vote = new NewsCommentVote(comment.id, true);
                                NewsDatabase.getInstance(context).voteDao().insert(vote);

                                binding.buttonLike.setEnabled(false);
                                binding.buttonDislike.setEnabled(false);

                                binding.buttonLike.setBackgroundResource(R.drawable.background_button_like);


                            } else {
//                                Toast.makeText(context, "NEUSPESNO: " + response.body().vote, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NewsCommentVote> call, Throwable t) {

                        }
                    });
        });

        binding.buttonDislike.setOnClickListener(view -> {

            NewsApi.getInstance().getNewsService().postDislike(Integer.parseInt(comment.id), false)
                    .enqueue(new Callback<NewsCommentVote>() {
                        @Override
                        public void onResponse(Call<NewsCommentVote> call, Response<NewsCommentVote> response) {
                            if (response.isSuccessful()) {
//                                Toast.makeText(context, "USPESNO: " + response.body().vote, Toast.LENGTH_SHORT).show();

                                int dislikes = Integer.parseInt(binding.textViewDislike.getText().toString());
                                dislikes++;
                                binding.textViewDislike.setText(String.valueOf(dislikes));

                                NewsCommentVote vote = new NewsCommentVote(comment.id, false);
                                NewsDatabase.getInstance(context).voteDao().insert(vote);

                                binding.buttonLike.setEnabled(false);
                                binding.buttonDislike.setEnabled(false);

                                binding.buttonDislike.setBackgroundResource(R.drawable.background_button_dislike);

                            } else {
//                                Toast.makeText(context, "NEUSPESNO: " + response.body().vote, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NewsCommentVote> call, Throwable t) {

                        }
                    });
        });

    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
