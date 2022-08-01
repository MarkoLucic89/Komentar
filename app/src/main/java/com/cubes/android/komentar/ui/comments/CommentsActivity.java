package com.cubes.android.komentar.ui.comments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.source.remote.networking.response.CommentsResponseModel;
import com.cubes.android.komentar.databinding.ActivityCommentsBinding;

import java.util.ArrayList;


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

        DataRepository.getInstance().getComments(news_id, new DataRepository.CommentsResponseListener() {


            @Override
            public void onResponse(ArrayList<NewsComment> response) {
                if (response.isEmpty()) {
                    binding.textView.setVisibility(View.VISIBLE);
                } else {
                    initRecyclerView(response);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void initRecyclerView(ArrayList<NewsComment> comments) {
        binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewComments.setAdapter(new CommentsAdapter(comments));
    }
}