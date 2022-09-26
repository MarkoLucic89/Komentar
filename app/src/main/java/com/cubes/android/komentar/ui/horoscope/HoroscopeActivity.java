package com.cubes.android.komentar.ui.horoscope;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.HoroscopeResponseModel;
import com.cubes.android.komentar.databinding.ActivityHoroscopeBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.squareup.picasso.Picasso;

public class HoroscopeActivity extends AppCompatActivity {

    private ActivityHoroscopeBinding binding;

    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHoroscopeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;

        sendRequest();

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewRefresh.setOnClickListener(view -> {
            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
            sendRequest();
        });

    }

    private void sendRequest() {
        binding.cardViewRoot.setVisibility(View.GONE);

        dataRepository.getHoroscope(new DataRepository.HoroscopeResponseListener() {
            @Override
            public void onResponse(HoroscopeResponseModel.HoroscopeDataResponseModel response) {

                binding.cardViewRoot.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                binding.textViewSign.setText(response.name);
                binding.textViewDate.setText(response.date);
                Picasso.get().load(response.image_url).into(binding.imageViewSign);
                binding.textViewContent.setText(response.horoscope);
            }

            @Override
            public void onFailure(Throwable t) {
                binding.cardViewRoot.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding = null;
//    }
}