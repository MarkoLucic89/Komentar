package com.cubes.android.komentar.ui.main.search.rv_model_search;

import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;
import com.cubes.android.komentar.ui.tag.TagAdapter;
import com.cubes.android.komentar.ui.tag.rv_model_tag.ItemModelTag;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelSearchAd implements ItemModelSearch {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(SearchAdapter.SearchViewHolder holder) {

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
