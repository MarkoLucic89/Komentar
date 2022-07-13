package com.cubes.android.komentar.data.model.rv_item_model.rv_item_details;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.response.comments_response.CommentsResponseModel;
import com.cubes.android.komentar.data.model.response.news_details_response.NewsDetailsDataResponseModel;
import com.cubes.android.komentar.databinding.RvItemDetailsCommentsBinding;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.activity.CommentsActivity;
import com.cubes.android.komentar.ui.adapter.CommentsAdapter;
import com.cubes.android.komentar.ui.adapter.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RvItemModelDetailsComments implements ItemModelDetails {

    private NewsDetailsDataResponseModel data;

    public RvItemModelDetailsComments(NewsDetailsDataResponseModel data) {
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
