package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.RvItemDetailsCommentsBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class RvItemModelDetailsComments implements ItemModelDetails {

    private NewsDetailsResponseModel.NewsDetailsDataResponseModel data;

    public RvItemModelDetailsComments(NewsDetailsResponseModel.NewsDetailsDataResponseModel data) {
        this.data = data;
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsCommentsBinding binding = (RvItemDetailsCommentsBinding) holder.binding;

        binding.textViewTitle.setText("Komentari (" + data.comments_count + ")");
        binding.textViewCommentsCount.setText(String.valueOf(data.comments_count));

//        NewsApi.getInstance().getNewsService().getComments(data.id).enqueue(new Callback<CommentsResponseModel>() {
//            @Override
//            public void onResponse(Call<CommentsResponseModel> call, Response<CommentsResponseModel> response) {
//
//                binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
//                binding.recyclerView.setAdapter(new CommentsAdapter(response.body().data, true));
//
//            }
//
//            @Override
//            public void onFailure(Call<CommentsResponseModel> call, Throwable t) {
//
//            }
//        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(new CommentsAdapter(data.comments_top_n, true));

        if (data.comments_count > 0) {
            binding.buttonAllComments.setVisibility(View.VISIBLE);
            binding.buttonAllComments.setOnClickListener(view -> MyMethodsClass.goToCommentsActivity(view, data.id));
        } else {
            binding.buttonAllComments.setVisibility(View.GONE);
        }

        binding.buttonPostComment.setOnClickListener(view ->  {
            MyMethodsClass.goToPostCommentsActivity(view, data.id, 0);
        });

    }


}
