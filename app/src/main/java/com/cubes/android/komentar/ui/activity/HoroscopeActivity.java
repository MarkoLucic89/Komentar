package com.cubes.android.komentar.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.ActivityHoroscopeBinding;

public class HoroscopeActivity extends AppCompatActivity {

    private ActivityHoroscopeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHoroscopeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}