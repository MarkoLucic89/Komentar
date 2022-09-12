package com.cubes.android.komentar.ui.main.search.rv_model_search;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.main.search.SearchAdapter;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelSearchAd implements ItemModelSearch {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(SearchAdapter.SearchViewHolder holder) {

        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;

//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

    }
}
