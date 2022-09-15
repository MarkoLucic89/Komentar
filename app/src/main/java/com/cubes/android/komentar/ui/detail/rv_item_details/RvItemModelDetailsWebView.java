package com.cubes.android.komentar.ui.detail.rv_item_details;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.databinding.RvItemDetailsWebViewBinding;
import com.cubes.android.komentar.ui.detail.NewsDetailsAdapter;

public class RvItemModelDetailsWebView implements ItemModelDetails {

    private String url;
    private int newsId;
    private NewsDetailsAdapter.WebViewListener webViewListener;


    public RvItemModelDetailsWebView(String url, int newsId, NewsDetailsAdapter.WebViewListener webViewListener) {
        this.url = url;
        this.newsId = newsId;
        this.webViewListener = webViewListener;
    }

    @Override
    public int getType() {
        return R.layout.rv_item_details_web_view;
    }

    @Override
    public void bind(NewsDetailsAdapter.NewsDetailsViewHolder holder) {

        RvItemDetailsWebViewBinding binding = (RvItemDetailsWebViewBinding) holder.binding;

        String url = DataContainer.BASE_URL + "/api/newswebview?id=" + newsId + "&version=2";
        binding.webView.loadUrl(url);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                webViewListener.onWebViewLoaded();
            }
        });
    }
}
