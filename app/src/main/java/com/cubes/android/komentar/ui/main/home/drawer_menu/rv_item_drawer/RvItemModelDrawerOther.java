package com.cubes.android.komentar.ui.main.home.drawer_menu.rv_item_drawer;


import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemDrawerOtherBinding;
import com.cubes.android.komentar.ui.currency_list.CurrencyListActivity;
import com.cubes.android.komentar.ui.horoscope.HoroscopeActivity;
import com.cubes.android.komentar.ui.main.home.drawer_menu.DrawerAdapter;
import com.cubes.android.komentar.ui.weather.WeatherActivity;


public class RvItemModelDrawerOther implements ItemModelDrawer {

    public String title;
    public boolean isPushNotificationsOn;
    private OnPushNotificationListener notificationListener;

    public interface OnPushNotificationListener {
        void onPushNotification(boolean isOn);
    }

    public RvItemModelDrawerOther(String title) {
        this.title = title;
    }

    public RvItemModelDrawerOther(OnPushNotificationListener pushNotificationListener, String title, boolean isNotificationsOn) {
        this.title = title;
        this.notificationListener = pushNotificationListener;
        this.isPushNotificationsOn = isNotificationsOn;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_drawer_other;
    }

    @Override
    public void bind(DrawerAdapter.DrawerViewHolder holder) {

        RvItemDrawerOtherBinding binding = (RvItemDrawerOtherBinding) holder.binding;

        setUI(binding);
    }

    private void setUI(RvItemDrawerOtherBinding binding) {

        binding.textView.setText(title);

        switch (title.toUpperCase()) {
            case "VREMENSKA PROGNOZA":

                binding.viewLine.setVisibility(View.VISIBLE);
                binding.switchPushNotifications.setVisibility(View.GONE);
                binding.viewEmptySpace.setVisibility(View.GONE);

                binding.getRoot().setOnClickListener(view -> goToActivity(title.toUpperCase().trim(), view));

                break;
            case "KURSNA LISTA":

                binding.viewLine.setVisibility(View.GONE);
                binding.switchPushNotifications.setVisibility(View.GONE);
                binding.viewEmptySpace.setVisibility(View.GONE);

                binding.getRoot().setOnClickListener(view -> {
//                    goToActivity(title.toUpperCase().trim(), view);

                    String url = "http://www.vipsistem.rs/kursna-lista.php";

                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    binding.getRoot().getContext().startActivity(myIntent);
                });

                break;
            case "HOROSKOP":

                binding.viewLine.setVisibility(View.GONE);
                binding.switchPushNotifications.setVisibility(View.GONE);
                binding.viewEmptySpace.setVisibility(View.GONE);

                binding.getRoot().setOnClickListener(view -> goToActivity(title.toUpperCase().trim(), view));

                break;
            case "PUSH NOTIFIKACIJE":

                binding.viewLine.setVisibility(View.VISIBLE);
                binding.switchPushNotifications.setVisibility(View.VISIBLE);
                binding.viewEmptySpace.setVisibility(View.GONE);

                binding.switchPushNotifications.setChecked(isPushNotificationsOn);

                binding.switchPushNotifications.setOnCheckedChangeListener((compoundButton, b) -> notificationListener.onPushNotification(b));

                break;
            case "MARKETING":

                binding.viewLine.setVisibility(View.GONE);
                binding.switchPushNotifications.setVisibility(View.GONE);
                binding.viewEmptySpace.setVisibility(View.GONE);

                binding.getRoot().setOnClickListener(view -> goToActivity(title.toUpperCase().trim(), view));

                break;
            case "USLOVI KORIŠĆENJA":

                binding.viewLine.setVisibility(View.GONE);
                binding.switchPushNotifications.setVisibility(View.GONE);
                binding.viewEmptySpace.setVisibility(View.GONE);

                binding.getRoot().setOnClickListener(view -> goToActivity(title.toUpperCase().trim(), view));

                break;
            case "KONTAKT":

                binding.viewLine.setVisibility(View.GONE);
                binding.switchPushNotifications.setVisibility(View.GONE);
                binding.viewEmptySpace.setVisibility(View.VISIBLE);

                binding.getRoot().setOnClickListener(view -> goToActivity(title.toUpperCase().trim(), view));

                break;
            default:

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
