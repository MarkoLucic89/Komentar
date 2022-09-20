package com.cubes.android.komentar.ui.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.WeatherResponseModel;
import com.cubes.android.komentar.databinding.ActivityWeatherBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.squareup.picasso.Picasso;

public class WeatherActivity extends AppCompatActivity {

    private ActivityWeatherBinding binding;

    private DataRepository dataRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;

        dataRepository.getWeather(new DataRepository.WeatherResponseListener() {
            @Override
            public void onResponse(WeatherResponseModel.WeatherResponseDataModel data) {
                binding.textViewName.setText(data.name);
                Picasso.get().load(data.icon_url).into(binding.imageView);
                binding.textViewTemperature.setText(data.temp);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


        binding.imageViewBack.setOnClickListener(view -> finish());

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding = null;
//    }
}