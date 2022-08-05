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
    private ArrayList<ItemModelComments> list = new ArrayList<>();

    private boolean isSize5;
    private CommentsListener listener;

    public CommentsAdapter(CommentsListener listener) {
        this.listener = listener;

    }

    public CommentsAdapter(ArrayList<NewsComment> comments, boolean isSize5) {
        addComments(comments);
        this.isSize5 = isSize5;
    }

    public void updateList(ArrayList<NewsComment> comments) {
        addComments(comments);
        notifyDataSetChanged();
    }

    public void addComments(ArrayList<NewsComment> comments) {

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
            return Math.min(list.size(), 5);
        }
        return list.size();
    }

    public void commentLiked(int id, boolean vote) {
        
        for (ItemModelComments itemModel : list) {
            
            if (itemModel.getCommentsId() == id) {

                itemModel.updateLikeUi();

                break;

            }
        }
    }

    public void commentDisliked(int id, boolean vote) {
        
        for (ItemModelComments itemModel : list) {

            if (itemModel.getCommentsId() == id) {

                itemModel.updateDislikeUi();

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
