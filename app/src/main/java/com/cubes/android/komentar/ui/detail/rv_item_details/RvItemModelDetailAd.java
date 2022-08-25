package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.ItemModelCategory;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelDetailAd implements ItemModelDetails {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder  holder) {

        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;


        binding.adView.setVisibility(View.GONE);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                binding.adView.setVisibility(View.VISIBLE);
            }
        });
    }
}
