package com.cubes.android.komentar.ui.main.latest.rv_model_category;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
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
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

    }
}
