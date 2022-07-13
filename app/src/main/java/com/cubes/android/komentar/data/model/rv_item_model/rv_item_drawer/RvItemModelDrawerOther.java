package com.cubes.android.komentar.data.model.rv_item_model.rv_item_drawer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.SharedPrefs;
import com.cubes.android.komentar.databinding.RvItemDrawerCategoryBinding;
import com.cubes.android.komentar.databinding.RvItemDrawerOtherBinding;
import com.cubes.android.komentar.listeners.OnCategoryClickListener;
import com.cubes.android.komentar.ui.activity.CurrencyListActivity;
import com.cubes.android.komentar.ui.activity.HoroscopeActivity;
import com.cubes.android.komentar.ui.activity.WeatherActivity;
import com.cubes.android.komentar.ui.adapter.DrawerAdapter;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;


public class RvItemModelDrawerOther implements ItemModelDrawer {

    public String title;
    public boolean isLineVisible;
    public boolean isLast;
    public boolean isPushNotifications;
    private Activity activity;

    public RvItemModelDrawerOther(String title, boolean isLineVisible) {
        this.title = title;
        this.isLineVisible = isLineVisible;
    }

    public RvItemModelDrawerOther(String title, boolean isLineVisible, boolean isLast) {
        this.title = title;
        this.isLineVisible = isLineVisible;
        this.isLast = isLast;
    }

    public RvItemModelDrawerOther(Activity activity, boolean isPushNotifications, String title, boolean isLineVisible) {
        this.activity = activity;
        this.isPushNotifications = isPushNotifications;
        this.title = title;
        this.isLineVisible = isLineVisible;

    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void bind(DrawerAdapter.DrawerViewHolder holder) {

        RvItemDrawerOtherBinding binding = (RvItemDrawerOtherBinding) holder.binding;

        binding.textView.setText(title);

        if (isPushNotifications) {

            binding.switchPushNotifications.setVisibility(View.VISIBLE);
            binding.switchPushNotifications.setChecked(SharedPrefs.getInstance(activity).isNotificationOn());
            binding.switchPushNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
                SharedPrefs.getInstance(activity).setNotificationStatus(b);
            });

        } else {

            binding.switchPushNotifications.setVisibility(View.GONE);
            binding.getRoot().setOnClickListener(view -> goToActivity(title.toUpperCase().trim(), view));

        }

        if (isLineVisible) {
            binding.viewLine.setVisibility(View.VISIBLE);
        } else {
            binding.viewLine.setVisibility(View.GONE);
        }

        if (isLast) {
            binding.viewEmptySpace.setVisibility(View.VISIBLE);
        } else {
            binding.viewEmptySpace.setVisibility(View.GONE);
        }


    }

    private void goToActivity(String title, View view) {

        switch (title) {
            case "VREMENSKA PROGNOZA":
                Intent intentWeather = new Intent(view.getContext(), WeatherActivity.class);
                view.getContext().startActivity(intentWeather);
                break;
            case "KURSNA LISTA":
                Intent intentCurrencyList = new Intent(view.getContext(), CurrencyListActivity.class);
                view.getContext().startActivity(intentCurrencyList);
                break;
            case "HOROSKOP":
                Intent intentHoroscope = new Intent(view.getContext(), HoroscopeActivity.class);
                view.getContext().startActivity(intentHoroscope);
                break;
            default:
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://komentar.rs/")));

        }
    }

}
