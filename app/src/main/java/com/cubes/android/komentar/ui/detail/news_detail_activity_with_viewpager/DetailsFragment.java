package com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.FragmentDetailsBinding;

public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;

    private static final String NEWS_ID = "news_id";

    private int newsId;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(int newsId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(NEWS_ID, newsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsId = getArguments().getInt(NEWS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataRepository.getInstance().sendNewsDetailsRequest(newsId, new DataRepository.DetailResponseListener() {
            @Override
            public void onResponse(NewsDetailsResponseModel.NewsDetailsDataResponseModel response) {

                String id = String.valueOf(response.id);
                String title = response.title;

                binding.textViewId.setText(id);
                binding.textViewTitle.setText(title);


            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }
}