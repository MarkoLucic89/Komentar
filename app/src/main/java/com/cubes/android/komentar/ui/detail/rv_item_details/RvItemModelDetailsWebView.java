package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.webkit.WebViewClient;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.databinding.RvItemDetailsWebViewBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;

public class RvItemModelDetailsWebView implements ItemModelDetails {

    public String url;
    public int newsId;

    public RvItemModelDetailsWebView(String url, int newsId) {
        this.url = url;
        this.newsId = newsId;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_details_web_view;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsWebViewBinding binding = (RvItemDetailsWebViewBinding) holder.binding;

        binding.webView.setWebViewClient(new WebViewClient());
//        binding.webView.loadUrl(url);

        String url = DataContainer.BASE_URL + "/api/newswebview?id=" + newsId + "&version=2";
        binding.webView.loadUrl(url);
    }
}
