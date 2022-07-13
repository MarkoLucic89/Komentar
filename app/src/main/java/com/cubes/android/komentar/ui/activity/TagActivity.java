package com.cubes.android.komentar.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.android.komentar.data.model.response.tag_response.TagResponseModel;
import com.cubes.android.komentar.databinding.ActivityTagBinding;
import com.cubes.android.komentar.networking.NewsApi;
import com.cubes.android.komentar.ui.adapter.SearchAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagActivity extends AppCompatActivity {

    private ActivityTagBinding binding;
    private int tagId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tagId = getIntent().getIntExtra("tag_id", -1);
        String tagTitle = getIntent().getStringExtra("tag_title");

        sendTagRequest();

        binding.textViewTitle.setText(tagTitle);
        binding.imageViewBack.setOnClickListener(view -> finish());
        binding.imageViewRefresh.setOnClickListener(view -> sendTagRequest());


    }

    private void sendTagRequest() {

        binding.imageViewRefresh.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        NewsApi.getInstance().getNewsService().getTag(tagId).enqueue(new Callback<TagResponseModel>() {
            @Override
            public void onResponse(Call<TagResponseModel> call, Response<TagResponseModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                binding.recyclerView.setLayoutManager(new LinearLayoutManager(TagActivity.this));
                binding.recyclerView.setAdapter(new SearchAdapter(response.body().data.news));
            }

            @Override
            public void onFailure(Call<TagResponseModel> call, Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
            sendTagRequest();
        }

    }
}