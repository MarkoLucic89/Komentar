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

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private ArrayList<ItemModelComments> list = new ArrayList<>();

    private final CommentsListener listener;

    //CommentsActivity constructor
    public CommentsAdapter(CommentsListener listener) {
        this.listener = listener;
    }

    //NewsDetailActivity constructor (NE KORISTI SE VISE)
    public CommentsAdapter(CommentsListener listener, ArrayList<NewsComment> comments) {
        updateList(comments);
        this.listener = listener;
    }

    public void updateList(ArrayList<NewsComment> comments) {

        list.clear();

        for (NewsComment comment : comments) {

            list.add(new RvItemModelComments(comment, listener, false));

            addChildren(comment.children);

        }

        notifyDataSetChanged();
    }

    private void addChildren(ArrayList<NewsComment> comments) {

        if (comments != null) {

            for (NewsComment comment : comments) {

                list.add(new RvItemModelComments(comment, listener, true));

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

    public interface CommentsListener {

        void onLikeListener(int id, boolean vote);

        void onDislikeListener(int id, boolean vote);

        void goOnPostCommentActivity(int newsId, int reply_id);

        default void goToCommentsActivity(int newsId) {

        }

    }
}
