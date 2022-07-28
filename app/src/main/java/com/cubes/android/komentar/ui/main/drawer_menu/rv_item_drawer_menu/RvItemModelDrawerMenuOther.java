package com.cubes.android.komentar.ui.main.drawer_menu.rv_item_drawer_menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.cubes.android.komentar.data.source.local.SharedPrefs;
import com.cubes.android.komentar.databinding.RvItemDrawerOtherBinding;
import com.cubes.android.komentar.ui.currencylist.CurrencyListActivity;
import com.cubes.android.komentar.ui.horoscope.HoroscopeActivity;
import com.cubes.android.komentar.ui.weather.WeatherActivity;
import com.cubes.android.komentar.ui.main.drawer_menu.DrawerMenuAdapter;

public class RvItemModelDrawerMenuOther implements ItemModelDrawerMenu {

    private int type;
    private Activity activity;

    public RvItemModelDrawerMenuOther(int type) {
        this.type = type;
    }

    public RvItemModelDrawerMenuOther(int type, Activity activity) {
        this.type = type;
        this.activity = activity;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void bind(DrawerMenuAdapter.DrawerMenuViewHolder holder) {

        RvItemDrawerOtherBinding binding = (RvItemDrawerOtherBinding) holder.binding;

        switch (type) {
            case 0:
                initWeather(binding);
                break;
            case 1:
                initCurrencyList(binding);
                break;
            case 2:
                initHoroscope(binding);
                break;
            case 3:
                initPushNotifications(binding, activity);
                break;
            case 4:
                initMarketing(binding);
                break;
            case 5:
                initTermsAndConditions(binding);
                break;
            case 6:
                initContact(binding);
                break;

        }
    }

    private void initWeather(RvItemDrawerOtherBinding binding) {

        binding.viewLine.setVisibility(View.VISIBLE);
        binding.textView.setText("VREMENSKA PROGNOZA");
        binding.switchPushNotifications.setVisibility(View.GONE);
        binding.viewEmptySpace.setVisibility(View.GONE);

        binding.getRoot().setOnClickListener(view -> {
            Intent intentWeather = new Intent(view.getContext(), WeatherActivity.class);
            view.getContext().startActivity(intentWeather);
        });

    }

    private void initCurrencyList(RvItemDrawerOtherBinding binding) {

        binding.viewLine.setVisibility(View.GONE);
        binding.textView.setText("KURSNA LISTA");
        binding.switchPushNotifications.setVisibility(View.GONE);
        binding.viewEmptySpace.setVisibility(View.GONE);

        binding.getRoot().setOnClickListener(view -> {
            Intent intentCurrencyList = new Intent(view.getContext(), CurrencyListActivity.class);
            view.getContext().startActivity(intentCurrencyList);
        });

    }

    private void initHoroscope(RvItemDrawerOtherBinding binding) {

        binding.viewLine.setVisibility(View.GONE);
        binding.textView.setText("HOROSKOP");
        binding.switchPushNotifications.setVisibility(View.GONE);
        binding.viewEmptySpace.setVisibility(View.GONE);

        binding.getRoot().setOnClickListener(view -> {
            Intent intentHoroscope = new Intent(view.getContext(), HoroscopeActivity.class);
            view.getContext().startActivity(intentHoroscope);
        });

    }

    private void initPushNotifications(RvItemDrawerOtherBinding binding, Activity activity) {

        binding.viewLine.setVisibility(View.VISIBLE);
        binding.textView.setText("PUSH NOTIFIKACIJE");
        binding.switchPushNotifications.setVisibility(View.VISIBLE);
        binding.viewEmptySpace.setVisibility(View.GONE);

        binding.switchPushNotifications.setChecked(SharedPrefs.getInstance(activity).isNotificationOn());

        binding.switchPushNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPrefs.getInstance(activity).setNotificationStatus(b);
        });

    }

    private void initMarketing(RvItemDrawerOtherBinding binding) {

        binding.viewLine.setVisibility(View.GONE);
        binding.textView.setText("MARKETING");
        binding.switchPushNotifications.setVisibility(View.GONE);
        binding.viewEmptySpace.setVisibility(View.GONE);

        binding.getRoot().setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://komentar.rs/")));
        });

    }

    private void initTermsAndConditions(RvItemDrawerOtherBinding binding) {

        binding.viewLine.setVisibility(View.GONE);
        binding.textView.setText("USLOVI KORIŠĆENJA");
        binding.switchPushNotifications.setVisibility(View.GONE);
        binding.viewEmptySpace.setVisibility(View.GONE);

        binding.getRoot().setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://komentar.rs/")));
        });

    }

    private void initContact(RvItemDrawerOtherBinding binding) {

        binding.viewLine.setVisibility(View.GONE);
        binding.textView.setText("KONTAKT");
        binding.switchPushNotifications.setVisibility(View.GONE);
        binding.viewEmptySpace.setVisibility(View.VISIBLE);

        binding.getRoot().setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://komentar.rs/")));
        });

    }

}
