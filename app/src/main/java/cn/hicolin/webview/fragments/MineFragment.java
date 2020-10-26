package cn.hicolin.webview.fragments;

import android.webkit.WebView;

public class MineFragment extends WebViewFragment {
    @Override
    public void loadUrl(WebView webView) {
        webView.loadUrl("https://cn.bing.com");
    }

}
