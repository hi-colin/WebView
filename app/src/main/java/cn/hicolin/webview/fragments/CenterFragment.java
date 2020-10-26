package cn.hicolin.webview.fragments;

import android.webkit.WebView;

public class CenterFragment extends WebViewFragment {
    @Override
    public void loadUrl(WebView webView) {
        webView.loadUrl("https://wap.sogou.com");
    }

}
