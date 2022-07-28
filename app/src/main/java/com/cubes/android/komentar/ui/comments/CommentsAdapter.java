package com.cubes.android.komentar.ui.comments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.ui.comments.rv_item_comments.RvItemModelComments;
import com.cubes.android.komentar.databinding.RvItemCommentBinding;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private ArrayList<RvItemModelComments> list;
    private int leftPadding = 0;
    private boolean isSize5;

    public CommentsAdapter(ArrayList<NewsComment> comments) {
        this.list = new ArrayList<>();

        for (NewsComment comment : comments) {

            leftPadding = 0;

            list.add(new RvItemModelComments(comment));

            addChildren(comment);

        }
    }

    public CommentsAdapter(ArrayList<NewsComment> comments, boolean isSize5) {
        this.isSize5 = isSize5;

        this.list = new ArrayList<>();

        for (NewsComment comment : comments) {

            leftPadding = 0;

            list.add(new RvItemModelComments(comment));

            addChildren(comment);

        }
    }

    private void addChildren(NewsComment comment) {

        if (comment.children == null || comment.children.isEmpty()) {
            return;
        }

        leftPadding += 80;

        for (NewsComment child : comment.children) {

            list.add(new RvItemModelComments(child, leftPadding));

            addChildren(child);


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
        if (isSize5 && list.size() > 5) {
            return 5;
        }
        return list.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public CommentsViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
