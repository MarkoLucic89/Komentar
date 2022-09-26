package com.cubes.android.komentar.ui.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.source.remote.networking.response.WeatherResponseModel;
import com.cubes.android.komentar.databinding.ActivityWeatherBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
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

        sendRequest();

        binding.imageViewBack.setOnClickListener(view -> finish());

        binding.imageViewRefresh.setOnClickListener(view -> {
            MyMethodsClass.startRefreshAnimation(binding.imageViewRefresh);
            sendRequest();
        });
    }

    private void sendRequest() {


        binding.cardViewRoot.setVisibility(View.GONE);

        dataRepository.getWeather(new DataRepository.WeatherResponseListener() {
            @Override
            public void onResponse(WeatherResponseModel.WeatherResponseDataModel data) {

                binding.cardViewRoot.setVisibility(View.VISIBLE);
                binding.imageViewRefresh.setVisibility(View.GONE);

                binding.textViewName.setText(data.name);
                Picasso.get().load(data.icon_url).into(binding.imageView);
                binding.textViewTemperature.setText(data.temp + "°");
                binding.textViewTempMin.setText("Min: " + data.temp_min);
                binding.textViewTempMax.setText("Max: " + data.temp_max);
                binding.textViewHumidity.setText("Atmosferski pritisak: " + data.humidity + " mb");
                binding.textViewWind.setText("Brzina vetra: " + data.wind + " m/s");

                Picasso.get().load(data.day_0.icon_url).into(binding.imageViewWeather1);
                binding.textViewTempDay1.setText(data.day_0.temp_min + " / " + data.day_0.temp_max);

                Picasso.get().load(data.day_1.icon_url).into(binding.imageViewWeather2);
                binding.textViewTempDay2.setText(data.day_1.temp_min + " / " + data.day_1.temp_max);

                Picasso.get().load(data.day_2.icon_url).into(binding.imageViewWeather3);
                binding.textViewTempDay3.setText(data.day_2.temp_min + " / " + data.day_2.temp_max);

                Picasso.get().load(data.day_3.icon_url).into(binding.imageViewWeather4);
                binding.textViewTempDay4.setText(data.day_3.temp_min + " / " + data.day_3.temp_max);

                Picasso.get().load(data.day_4.icon_url).into(binding.imageViewWeather5);
                binding.textViewTempDay5.setText(data.day_4.temp_min + " / " + data.day_4.temp_max);

                Picasso.get().load(data.day_5.icon_url).into(binding.imageViewWeather6);
                binding.textViewTempDay6.setText(data.day_5.temp_min + " / " + data.day_5.temp_max);

                Picasso.get().load(data.day_6.icon_url).into(binding.imageViewWeather7);
                binding.textViewTempDay7.setText(data.day_6.temp_min + " / " + data.day_6.temp_max);

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