package com.cubes.android.komentar.ui.main.videos.rv_model_videos;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.ui.main.videos.VideosAdapter;
import com.google.android.gms.ads.AdRequest;

public class RvItemModelVideoAd implements ItemModelVideo {


    @Override
    public int getType() {
        return R.layout.rv_item_ad;
    }

    @Override
    public void bind(VideosAdapter.VideosViewHolder holder) {

        RvItemAdBinding binding = (RvItemAdBinding) holder.binding;

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

    }
}
