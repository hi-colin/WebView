package cn.hicolin.webview.fragments;

import android.webkit.WebView;

public class HomeFragment extends WebViewFragment {
    @Override
    public void loadUrl(WebView webView) {
        webView.loadUrl("https://quark.sm.cn");
    }

}
