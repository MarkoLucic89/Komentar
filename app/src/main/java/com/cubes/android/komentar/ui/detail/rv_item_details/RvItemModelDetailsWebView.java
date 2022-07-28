package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.webkit.WebViewClient;

import com.cubes.android.komentar.databinding.RvItemDetailsWebViewBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;

public class RvItemModelDetailsWebView implements ItemModelDetails {

    public String url;

    public RvItemModelDetailsWebView(String url) {
        this.url = url;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsWebViewBinding binding = (RvItemDetailsWebViewBinding) holder.binding;

        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl(url);
    }
}
