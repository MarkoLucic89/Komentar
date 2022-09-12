package com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.HomePagerAdapter;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelHomeAd implements ItemModelHome {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(HomePagerAdapter.HomeViewHolder holder) {

        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;

//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

    }
}
