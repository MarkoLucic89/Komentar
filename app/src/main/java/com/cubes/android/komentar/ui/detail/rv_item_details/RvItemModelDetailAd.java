package com.cubes.android.komentar.ui.detail.rv_item_details;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelDetailAd implements ItemModelDetails {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder  holder) {

//        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

    }
}
