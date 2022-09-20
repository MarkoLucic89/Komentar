package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelHomeAd implements ItemModelCategory {

    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(CategoryAdapter.CategoryViewHolder  holder) {

//        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;
//
//        binding.shimmerLayout.setVisibility(View.VISIBLE);
//        binding.shimmerLayout.startLayoutAnimation();
//        binding.adView.setVisibility(View.GONE);
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);
//
//        binding.adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//
//                binding.shimmerLayout.stopShimmer();
//                binding.shimmerLayout.setVisibility(View.GONE);
//                binding.adView.setVisibility(View.VISIBLE);
//
//            }
//        });



    }
}
