package com.cubes.android.komentar.ui.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.NewsTag;
import com.cubes.android.komentar.databinding.RvItemTagBinding;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class NewsDetailsTagsAdapter extends RecyclerView.Adapter<NewsDetailsTagsAdapter.NewsDetailsTagsViewHolder> {

    private ArrayList<NewsTag> tags;

    public NewsDetailsTagsAdapter(ArrayList<NewsTag> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public NewsDetailsTagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsDetailsTagsViewHolder(
                RvItemTagBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDetailsTagsViewHolder holder, int position) {
        NewsTag tag = tags.get(position);

        holder.binding.textViewTitle.setText(tag.title);
        holder.binding.textViewTitle.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.blue_dark));
        holder.binding.rootLayout.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.grey_tag));


        holder.binding.getRoot().setOnClickListener(view -> {

            MyMethodsClass.goToTagActivity(view, tag.id, tag.title);

            holder.binding.textViewTitle.setTextColor(view.getContext().getResources().getColor(R.color.grey_tag));
            holder.binding.rootLayout.setBackgroundColor(view.getContext().getResources().getColor(R.color.blue_dark));

            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class NewsDetailsTagsViewHolder extends RecyclerView.ViewHolder {

        public RvItemTagBinding binding;

        public NewsDetailsTagsViewHolder(@NonNull RvItemTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
