package com.cubes.android.komentar.ui.tag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.TagResponseModel;
import com.cubes.android.komentar.databinding.ActivityTagBinding;
import com.cubes.android.komentar.ui.main.latest.LoadNextPageListener;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;


public class TagActivity extends AppCompatActivity implements LoadNextPageListener {

    private ActivityTagBinding binding;
    private int tagId;
    private int page = 1;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tagId = getIntent().getIntExtra("tag_id", -1);
        String tagTitle = getIntent().getStringExtra("tag_title");


        initRecyclerView();

        sendTagRequest();

        binding.textViewTitle.setText(tagTitle);
        binding.imageViewBack.setOnClickListener(view -> finish());
//        binding.imageViewRefresh.setOnClickListener(view -> sendTagRequest());
        binding.imageViewRefresh.setOnClickListener(view -> {
            if (page == 1) {
                sendTagRequest();
            } else {
                loadNextPage();
            }
        });


    }

    private void initRecyclerView() {
        adapter = new SearchAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(TagActivity.this));
        binding.recyclerView.setAdapter(adapter);
    }


    private void sendTagRequest() {

        DataRepository.getInstance().sendTagRequest(tagId, page, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(TagResponseModel.TagDataResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                if (binding.recyclerView.getVisibility() == View.GONE) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
                    binding.imageViewRefresh.setVisibility(View.GONE);
                }

                page++;

                adapter.updateList(response);

            }

            @Override
            public void onFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void loadNextPage() {

        binding.progressBar.setVisibility(View.VISIBLE);

        DataRepository.getInstance().sendTagRequest(tagId, page, new DataRepository.TagResponseListener() {

            @Override
            public void onResponse(TagResponseModel.TagDataResponseModel response) {

                binding.progressBar.setVisibility(View.GONE);

                if (binding.recyclerView.getVisibility() == View.GONE) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                if (binding.imageViewRefresh.getVisibility() == View.VISIBLE) {
                    binding.imageViewRefresh.setVisibility(View.GONE);
                }

                page++;

                adapter.loadNextPage(response);
            }

            @Override
            public void onFailure(Throwable t) {

                binding.recyclerView.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}