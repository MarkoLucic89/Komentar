package com.cubes.android.komentar.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cubes.android.komentar.databinding.ActivityCurrencyListBinding;

public class CurrencyListActivity extends AppCompatActivity {

    private ActivityCurrencyListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCurrencyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}