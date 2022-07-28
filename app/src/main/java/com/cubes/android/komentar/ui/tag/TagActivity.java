package com.cubes.android.komentar.ui.tag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.tag_response.TagResponseModel;
import com.cubes.android.komentar.databinding.ActivityTagBinding;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;


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

        DataRepository.getInstance().sendTagRequest(tagId, new DataRepository.TagResponseListener() {
            @Override
            public void onResponse(TagResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                binding.recyclerView.setLayoutManager(new LinearLayoutManager(TagActivity.this));
                binding.recyclerView.setAdapter(new SearchAdapter(response.data.news));
            }

            @Override
            public void onFailure(Throwable t) {

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