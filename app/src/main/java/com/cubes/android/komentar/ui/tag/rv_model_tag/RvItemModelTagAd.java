package com.cubes.android.komentar.ui.tag.rv_model_tag;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.tag.TagAdapter;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelTagAd implements ItemModelTag {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(TagAdapter.TagViewHolder  holder) {

        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;

//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

    }
}
