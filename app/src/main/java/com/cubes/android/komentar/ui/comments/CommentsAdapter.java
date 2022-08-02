package com.cubes.android.komentar.ui.comments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.databinding.RvItemCommentBinding;
import com.cubes.android.komentar.ui.comments.rv_item_comments.ItemModelComments;
import com.cubes.android.komentar.ui.comments.rv_item_comments.RvItemModelComments;
import com.cubes.android.komentar.ui.comments.rv_item_comments.RvItemModelSubComments;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private ArrayList<ItemModelComments> list;
    private boolean isSize5;
    private CommentsListener listener;

    public CommentsAdapter(ArrayList<NewsComment> comments) {

        addComments(comments);
    }

    public CommentsAdapter(ArrayList<NewsComment> comments, CommentsListener listener) {
        this.listener = listener;
        addComments(comments);
    }

    public void addComments(ArrayList<NewsComment> comments) {

        this.list = new ArrayList<>();

        for (NewsComment comment : comments) {

            list.add(new RvItemModelComments(comment, listener));

            addChildren(comment.children);

        }
    }

    private void addChildren(ArrayList<NewsComment> comments) {

        if (comments != null && !comments.isEmpty()) {

            for (NewsComment comment : comments) {

                list.add(new RvItemModelSubComments(comment, listener));

                addChildren(comment.children);

            }

        }

    }


    public CommentsAdapter(ArrayList<NewsComment> comments, boolean isSize5) {
        this.list = new ArrayList<>();

        for (NewsComment comment : comments) {

            list.add(new RvItemModelComments(comment));

            for (NewsComment subComment : comment.children) {

                list.add(new RvItemModelSubComments(subComment, listener));

                NewsComment newsComment = subComment;

                for (NewsComment childComment : newsComment.children) {

                    list.add(new RvItemModelSubComments(childComment, listener));

                }
            }

        }
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsViewHolder(
                RvItemCommentBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        list.get(position).bind(holder);

    }

    @Override
    public int getItemCount() {
        if (isSize5) {
            return 5;
        }
        return list.size();
    }

    public void commentLiked(int id, boolean vote) {
        
        for (ItemModelComments itemModel : list) {
            
            if (itemModel.getCommentsId() == id) {

                if (itemModel instanceof RvItemModelComments) {

                    RvItemModelComments tempModel = (RvItemModelComments) itemModel;

                    tempModel.updateLikedUi();

                } else {

                    RvItemModelSubComments tempModel = (RvItemModelSubComments) itemModel;

                    tempModel.updateLikedUi();

                }

                break;

            }
        }


    }

    public void commentDisliked(int id, boolean vote) {
        
        for (ItemModelComments itemModel : list) {

            if (itemModel.getCommentsId() == id) {

                if (itemModel instanceof RvItemModelComments) {

                    RvItemModelComments tempModel = (RvItemModelComments) itemModel;

                    tempModel.updateDislikedUi();

                } else {

                    RvItemModelSubComments tempModel = (RvItemModelSubComments) itemModel;

                    tempModel.updateDislikedUi();

                }

                break;

            }

        }

    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public CommentsViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
