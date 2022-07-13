package com.cubes.android.komentar.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.model.response.comments_response.CommentsResponseModel;
import com.cubes.android.komentar.databinding.ActivityCommentsBinding;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.CommentsAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity {

    private ActivityCommentsBinding binding;
    private int news_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        news_id = getIntent().getIntExtra("news_id", -1);

        binding.imageViewBack.setOnClickListener(view -> finish());

        NewsApi.getInstance().getNewsService().getComments(news_id).enqueue(new Callback<CommentsResponseModel>() {
            @Override
            public void onResponse(Call<CommentsResponseModel> call, Response<CommentsResponseModel> response) {

                if (response.body().data.isEmpty()) {
                    binding.textView.setVisibility(View.VISIBLE);
                } else {
                    initRecyclerView(response.body().data);
                }

            }

            @Override
            public void onFailure(Call<CommentsResponseModel> call, Throwable t) {
//                Toast.makeText(CommentsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initRecyclerView(ArrayList<NewsComment> comments) {
        binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewComments.setAdapter(new CommentsAdapter(comments));
    }
}